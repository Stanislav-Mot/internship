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
    private boolean enable;

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
            this.enable = true;

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        sleep(interval * 1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    updateCache();
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }

    private void put(Long key, Class<? extends Assignment> clazz, CacheObject value) {
        cacheMap.put(new KeyObject(key, clazz), value);
    }

    private void put(KeyObject keyObject, CacheObject cacheObject) {
        cacheMap.put(keyObject, cacheObject);
    }

    public void remove(Long id, Class<? extends Assignment> clazz) {
        cacheMap.remove(new KeyObject(id, clazz));
    }

    private void addAll() {
        List<GroupDto> groups = groupRepo.findAllWithoutConstraint().stream().map(x -> {
            GroupDto groupDto = new GroupDto();
            groupDto.setName(x.getName());
            groupDto.setId(x.getId());
            return groupDto;
        }).collect(Collectors.toList());

        List<TaskDto> tasks = taskRepo.findAllWithoutConstraint()
                .stream().map(taskMapper::convertToDto).collect(Collectors.toList());

        List<Composite> composites = compositeRepo.findAll();

        extractedGroups(groups, composites);
        extractedTasks(tasks, composites);
    }

    private void updateCache() {
        List<Long> taskIds = new ArrayList<>();
        List<Long> groupIds = new ArrayList<>();
        cacheMap.entrySet().parallelStream().filter(object -> !object.getValue().valid).forEach(x -> {
            if (x.getKey().clazz.equals(Task.class)) {
                taskIds.add(x.getKey().key);
            } else {
                groupIds.add(x.getKey().key);
            }
        });
        if (taskIds.isEmpty() && groupIds.isEmpty()) {
            return;
        }
        List<Composite> composites = compositeRepo.findAll();

        if (!taskIds.isEmpty()) {
            List<TaskDto> tasks = taskRepo.findAllWithoutConstraintByIds(taskIds)
                    .stream().map(taskMapper::convertToDto).collect(Collectors.toList());
            extractedTasks(tasks, composites);
        }
        if (!groupIds.isEmpty()) {
            List<GroupDto> groups = groupRepo.findAllWithoutConstraintByIds(groupIds)
                    .stream().map(x -> {
                        GroupDto groupDto = new GroupDto();
                        groupDto.setName(x.getName());
                        groupDto.setId(x.getId());
                        return groupDto;
                    }).collect(Collectors.toList());
            extractedGroups(groups, composites);
        }
    }

    private void extractedTasks(List<TaskDto> tasks, List<Composite> composites) {
        tasks.stream()
                .map(taskDto -> {
                    List<Composite> comp;
                    comp = composites.stream()
                            .filter(x -> x.getTaskId() != null && x.getTaskId().equals(taskDto.getId()))
                            .collect(Collectors.toList());
                    return new CacheObject(comp, taskDto, true);
                })
                .forEach(x -> put(((TaskDto) x.value).getId(), Task.class, x));
    }

    private void extractedGroups(List<GroupDto> groups, List<Composite> composites) {
        groups.stream()
                .map(groupDto -> {
                    List<Composite> comp;
                    comp = composites.stream()
                            .filter(x -> x.getGroupId().equals(groupDto.getId()))
                            .collect(Collectors.toList());
                    return new CacheObject(comp, groupDto, true);
                })
                .forEach(x -> put(((GroupDto) x.value).getId(), Group.class, x));
    }

    public Assignment getTask(Long id) {
        if (enable) {
            CacheObject cacheObject = cacheMap.get(new KeyObject(id, Task.class));
            if (cacheObject != null && cacheObject.valid) {
                return cacheObject.value;
            }
        }
        Task task = taskRepo.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Task Id %d is not found", id)));
        return taskMapper.convertToDto(task);
    }

    public Assignment getGroup(Long id) {
        if (enable) {
            CacheObject cacheObject = cacheMap.get(new KeyObject(id, Group.class));
            if (cacheObject != null && cacheObject.valid) {
                GroupDto groupDto = (GroupDto) cacheObject.getValue();
                groupDto.setAssignments(getComposite(cacheObject));
                return groupDto;
            }
        }
        Group group = groupRepo.findById(id).orElseThrow(() -> new DataNotFoundException(String.format("Group Id %d is not found", id)));
        return groupMapper.convertToDto(group);
    }

    public List<TaskDto> getAllTask() {
        if (enable) {
            return cacheMap.entrySet().parallelStream()
                    .filter(x -> x.getKey().getClazz().equals(Task.class) && x.getValue().valid)
                    .map(x -> (TaskDto) x.getValue().getValue())
                    .collect(Collectors.toList());
        }
        return taskRepo.findAll().stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    public List<GroupDto> getAllGroup() {
        if (enable) {
            return cacheMap.entrySet().parallelStream()
                    .filter(x -> x.getKey().getClazz().equals(Group.class) && x.getValue().valid)
                    .map(x -> {
                        GroupDto groupDto = (GroupDto) x.getValue().getValue();
                        groupDto.setAssignments(getComposite(x.getValue()));
                        return (GroupDto) x.getValue().getValue();
                    }).collect(Collectors.toList());
        }
        return groupRepo.findAll().stream().map(groupMapper::convertToDto).collect(Collectors.toList());
    }

    private List<Assignment> getComposite(CacheObject object) {
        List<Composite> composites = object.getComposites();

        List<Assignment> assignments = composites.stream().filter(x -> x.getTaskId() != null)
                .map(x -> cacheMap.get(new KeyObject(x.getTaskId(), Task.class)).value)
                .collect(Collectors.toList());

        List<Assignment> groups = composites.stream().filter(x -> x.getChildrenId() != null)
                .map(x -> {
                    CacheObject cacheObject = cacheMap.get(new KeyObject(x.getChildrenId(), Group.class));
                    GroupDto groupDto = (GroupDto) cacheObject.value;
                    groupDto.setAssignments(getComposite(cacheObject));
                    return groupDto;
                })
                .collect(Collectors.toList());
        assignments.addAll(groups);

        return assignments;
    }

    public List<Assignment> findByPersonId(Long id) {
        if (enable) {
            return cacheMap.entrySet().stream()
                    .filter(obj -> obj.getKey().getClazz().equals(Group.class) && obj.getValue().valid)
                    .filter(object -> object.getValue().getComposites()
                            .stream().anyMatch(composite -> composite.getPersonId() != null && composite.getPersonId().equals(id)))
                    .map(object -> (GroupDto) object.getValue().value)
                    .peek(groupDto -> groupDto.setAssignments(getComposite(cacheMap.get(new KeyObject(groupDto.getId(), Group.class)))))
                    .collect(Collectors.toList());
        }
        return groupRepo.findByPersonId(id)
                .stream().map(groupMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTaskByGroupId(Long id) {
        if (enable) {
            return cacheMap.entrySet().stream()
                    .filter(obj -> obj.getKey().getClazz().equals(Task.class) && obj.getValue().valid)
                    .filter(object -> object.getValue().getComposites()
                            .stream().anyMatch(composite -> composite.getGroupId() != null && composite.getGroupId().equals(id)))
                    .map(object -> (TaskDto) object.getValue().value)
                    .collect(Collectors.toList());
        }
        return taskRepo.findByGroupsId(id).stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }

    public List<TaskDto> getTaskByPersonId(Long id) {
        if (enable) {
            List<CacheObject> groups = cacheMap.values().stream()
                    .filter(object -> object.valid && object.getComposites()
                            .stream().anyMatch(x -> x.getPersonId() != null && x.getPersonId().equals(id)))
                    .collect(Collectors.toList());
            List<TaskDto> tasks = new ArrayList<>();

            groups.forEach(x -> tasks.addAll(getTaskByGroupId(((GroupDto) x.value).getId())));

            groups.forEach(x -> getChildren(((GroupDto) x.value).getId())
                    .forEach(taskId -> tasks.addAll(getTaskByGroupId(taskId))));

            return tasks.stream()
                    .collect(collectingAndThen(toCollection(() ->
                            new TreeSet<>(Comparator.comparingLong(TaskDto::getId))), ArrayList::new));
        }
        List<TaskDto> taskDtos = new ArrayList<>();
        List<Long> groupIds = taskRepo.findGroupIdByPersonId(id);
        groupIds.forEach(x ->
                taskDtos.addAll(taskRepo.findByGroupId(x).stream().map(taskMapper::convertToDto).collect(Collectors.toList())));
        return taskDtos;
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
                .filter(composite -> composite.getChildrenId() != null)
                .forEach(composite -> {
                    ids.add(composite.getChildrenId());
                    ids.addAll(getChildren(composite.getChildrenId()));
                });
        return ids;
    }

    public void addAssignment(Assignment assignment, Class<? extends Assignment> clazz) {
        List<Composite> composites = new ArrayList<>();
        CacheObject cacheObject = new CacheObject(composites, assignment, true);
        Long id;
        if (clazz.equals(Task.class)) {
            id = ((TaskDto) assignment).getId();
        } else {
            id = ((GroupDto) assignment).getId();
        }
        KeyObject keyObject = new KeyObject(id, clazz);
        put(keyObject, cacheObject);
    }

    public void setInvalid(Long id, Class<? extends Assignment> clazz) {
        KeyObject keyObject = new KeyObject(id, clazz);
        cacheMap.get(keyObject).setValid(false);
    }

    @Data
    @AllArgsConstructor
    private class CacheObject {
        private List<Composite> composites;
        private Assignment value;
        private Boolean valid;
    }

    @Data
    @AllArgsConstructor
    private class KeyObject {
        private Long key;
        private Class<? extends Assignment> clazz;
    }
}