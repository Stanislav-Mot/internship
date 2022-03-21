package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.exeption.DataNotFoundException;
import com.internship.internship.mapper.GroupMapper;
import com.internship.internship.mapper.TaskMapper;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Composite;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.CompositeRepo;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.TaskRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Transactional
@Service
public class CacheService {
    private final Map<KeyObject, CacheObject> cacheMap = new ConcurrentHashMap<>();
    private final TaskRepo taskRepo;
    private final GroupRepo groupRepo;
    private final TaskMapper taskMapper;
    private final GroupMapper groupMapper;
    private final CompositeRepo compositeRepo;
    private boolean valid;

    public CacheService(
            TaskRepo taskRepo,
            GroupRepo groupRepo,
            TaskMapper taskMapper,
            GroupMapper groupMapper,
            CompositeRepo compositeRepo,
            @Value(value = "${custom.cache}") Boolean cache,
            @Value(value = "${custom.interval}") Long interval) {
        this.taskRepo = taskRepo;
        this.groupRepo = groupRepo;
        this.taskMapper = taskMapper;
        this.groupMapper = groupMapper;
        this.compositeRepo = compositeRepo;

        if (Boolean.TRUE.equals(cache)) {
            addAll();
            this.valid = true;

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        sleep(interval * 1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    if (!valid) {
//                        addAll();
                        this.valid = true;
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        } else {
            this.valid = false;
        }
    }

    public void put(Long key, Class<? extends Assignment> clazz, CacheObject value) {
        cacheMap.put(new KeyObject(key, clazz), value);
    }

    public void remove(Long id, Class<? extends Assignment> clazz) {
        cacheMap.remove(new KeyObject(id, clazz));
    }

    public int size() {
        return cacheMap.size();
    }

    public void addAll() {
        cacheMap.clear();

        List<Group> groups = groupRepo.findAllWithoutConstraint();
        List<Task> tasks = taskRepo.findAllWithoutConstraint();
        List<Composite> composites = compositeRepo.findAll();

        groups.stream()
                .map(groupMapper::convertToDto)
                .map(groupDto -> {
                    List<Composite> comp;
                    comp = composites.stream()
                            .filter(x -> x.getGroup_id().equals(groupDto.getId()))
                            .collect(Collectors.toList());
                    return new CacheObject(comp, groupDto);
                })
                .forEach(x -> put(((GroupDto) x.value).getId(), Group.class, x));

        tasks.stream()
                .map(taskMapper::convertToDto)
                .map(taskDto -> {
                    List<Composite> comp;
                    comp = composites.stream()
                            .filter(x -> x.getTask_id() != null && x.getTask_id().equals(taskDto.getId()))
                            .collect(Collectors.toList());
                    return new CacheObject(comp, taskDto);
                })
                .forEach(x -> put(((TaskDto) x.value).getId(), Task.class, x));
    }

    public Assignment getTask(Long id) {
        if (valid) {
            return cacheMap.get(new KeyObject(id, Task.class)).value;
        } else {
            Task task = taskRepo.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Task Id %d is not found", id)));
            return taskMapper.convertToDto(task);
        }
    }

    public Assignment getGroup(Long id) {
        CacheObject c = cacheMap.get(new KeyObject(id, Group.class));
        if (c != null) {
            GroupDto groupDto = (GroupDto) c.getValue();
            groupDto.setAssignments(getComposite(c));
            return groupDto;
        } else {
            Group group = groupRepo.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Group Id %d is not found", id)));
            return groupMapper.convertToDto(group);
        }
    }

    public List<TaskDto> getAllTask() {
        if (valid) {
            return cacheMap.entrySet().parallelStream()
                    .filter(x -> x.getKey().getClazz().equals(Task.class))
                    .map(x -> (TaskDto) x.getValue().getValue())
                    .collect(Collectors.toList());
        } else {
            return taskRepo.findAll().stream().map(taskMapper::convertToDto).collect(Collectors.toList());
        }
    }

    public List<GroupDto> getAllGroup() {
        if (valid) {
            return cacheMap.entrySet().parallelStream()
                    .filter(x -> x.getKey().getClazz().equals(Group.class)).map(x -> {
                        GroupDto groupDto = (GroupDto) x.getValue().getValue();
                        groupDto.setAssignments(getComposite(x.getValue()));
                        return (GroupDto) x.getValue().getValue();
                    }).collect(Collectors.toList());
        } else {
            return groupRepo.findAll().stream().map(groupMapper::convertToDto).collect(Collectors.toList());
        }
    }

    private List<Assignment> getComposite(CacheObject object) {
        List<Composite> composites = object.getComposites();

        List<Assignment> assignments = composites.stream().filter(x -> x.getTask_id() != null)
                .map(x -> cacheMap.get(new KeyObject(x.getTask_id(), Task.class)).value)
                .collect(Collectors.toList());

        List<Assignment> groups = composites.stream().filter(x -> x.getChildren_id() != null)
                .map(x -> {
                    CacheObject cacheObject = cacheMap.get(new KeyObject(x.getChildren_id(), Group.class));
                    GroupDto groupDto = (GroupDto) cacheObject.value;
                    groupDto.setAssignments(getComposite(cacheObject));
                    return groupDto;
                })
                .collect(Collectors.toList());
        assignments.addAll(groups);

        return assignments;
    }

    public List<Assignment> findByPersonId(Long id) {
        if (valid) {
            return cacheMap.entrySet().stream()
                    .filter(obj -> obj.getKey().getClazz().equals(Group.class))
                    .filter(object -> object.getValue().getComposites()
                            .stream().anyMatch(composite -> composite.getPerson_id() != null && composite.getPerson_id().equals(id)))
                    .map(object -> (GroupDto) object.getValue().value)
                    .peek(groupDto -> groupDto.setAssignments(getComposite(cacheMap.get(new KeyObject(groupDto.getId(), Group.class)))))
                    .collect(Collectors.toList());
        } else {
            return groupRepo.findByPersonId(id)
                    .stream().map(groupMapper::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    public List<TaskDto> getTaskByGroupId(Long id) {
        if (valid) {
            return cacheMap.entrySet().stream()
                    .filter(obj -> obj.getKey().getClazz().equals(Task.class))
                    .filter(object -> object.getValue().getComposites()
                            .stream().anyMatch(composite -> composite.getGroup_id() != null && composite.getGroup_id().equals(id)))
                    .map(object -> (TaskDto) object.getValue().value)
                    .collect(Collectors.toList());
        } else {
            return taskRepo.findByGroupsId(id).stream().map(taskMapper::convertToDto).collect(Collectors.toList());
        }
    }

    public List<TaskDto> getTaskByPersonId(Long id) {
        if (valid) {
            List<CacheObject> groups = cacheMap.values().stream()
                    .filter(object -> object.getComposites()
                            .stream().anyMatch(x -> x.getPerson_id() != null && x.getPerson_id().equals(id)))
                    .collect(Collectors.toList());
            List<TaskDto> tasks = new ArrayList<>();

            groups.forEach(x -> tasks.addAll(getTaskByGroupId(((GroupDto) x.value).getId())));

            groups.forEach(x -> getChildren(((GroupDto) x.value).getId())
                    .forEach(taskId -> tasks.addAll(getTaskByGroupId(taskId))));

            return tasks.stream()
                    .collect(collectingAndThen(toCollection(() ->
                            new TreeSet<>(Comparator.comparingLong(TaskDto::getId))), ArrayList::new));
        } else {
            List<TaskDto> taskDtos = new ArrayList<>();
            List<Long> groupIds = taskRepo.findGroupIdByPersonId(id);
            groupIds.forEach(x ->
                    taskDtos.addAll(taskRepo.findByGroupId(x).stream().map(taskMapper::convertToDto).collect(Collectors.toList())));
            return taskDtos;
        }
    }

    private List<Long> getChildren(Long id) {
        List<Long> ids = new ArrayList<>();
        CacheObject parent;
        Optional<Map.Entry<KeyObject, CacheObject>> first = cacheMap.entrySet().stream()
                .filter(x -> x.getKey().equals(new KeyObject(id, Group.class))).findFirst();
        if (first.isPresent()) {
            parent = first.get().getValue();
        } else {
            return ids;
        }
        parent.getComposites().stream()
                .filter(composite -> composite.getChildren_id() != null)
                .forEach(composite -> {
                    ids.add(composite.getChildren_id());
                    ids.addAll(getChildren(composite.getChildren_id()));
                });
        return ids;
    }

    @Data
    @AllArgsConstructor
    private class CacheObject {
        private List<Composite> composites;
        private Assignment value;
    }

    @Data
    @AllArgsConstructor
    private class KeyObject {
        private Long key;
        private Class<? extends Assignment> clazz;
    }
}
