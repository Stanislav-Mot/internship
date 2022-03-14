package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.mapper.GroupDtoMapper;
import com.internship.internship.mapper.TaskDtoMapper;
import com.internship.internship.model.Assignment;
import com.internship.internship.model.Group;
import com.internship.internship.model.Task;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.TaskRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Transactional
@Service
public class CacheService {
    //time in seconds
    private static final long TIME_INTERVAL_FOR_CHECK_VALID = 15;
    private final Map<KeyObject, CacheObject> cacheMap = new ConcurrentHashMap<>();
    private final TaskRepo taskRepo;
    private final GroupRepo groupRepo;
    private final TaskDtoMapper taskDtoMapper;
    private final GroupDtoMapper groupDtoMapper;
    private boolean valid;

    public CacheService(TaskRepo taskRepo, GroupRepo groupRepo, TaskDtoMapper taskDtoMapper, GroupDtoMapper groupDtoMapper) {
        this.taskRepo = taskRepo;
        this.groupRepo = groupRepo;
        this.taskDtoMapper = taskDtoMapper;
        this.groupDtoMapper = groupDtoMapper;

        addAll();
        this.valid = true;

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TIME_INTERVAL_FOR_CHECK_VALID * 1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                if (!valid) {
                    addAll();
                    this.valid = true;
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void put(Long key, Class<? extends Assignment> clazz, Assignment value) {
        cacheMap.put(new KeyObject(key, clazz), new CacheObject(value));
    }

    public void remove(Long id, Class<? extends Assignment> clazz) {
        cacheMap.remove(new KeyObject(id, clazz));
    }

    public int size() {
        return cacheMap.size();
    }

    public void addAll() {
        cacheMap.clear();
        groupRepo.findAll().stream()
                .map(group -> {
                    group.setTasks(getComposite(group.getId()));
                    return groupDtoMapper.convertToDto(group);
                })
                .forEach(x -> put(x.getId(), Group.class, x));
        taskRepo.findAll().stream()
                .map(taskDtoMapper::convertToDto)
                .forEach(x -> put(x.getId(), Task.class, x));
        System.out.println();
    }

    public Assignment getTask(Long id) {
        CacheObject c = cacheMap.get(new KeyObject(id, Task.class));
        c.lastAccessed = System.currentTimeMillis();
        return c.value;
    }

    public Assignment getGroup(Long id) {
        CacheObject c = cacheMap.get(new KeyObject(id, Group.class));
        c.lastAccessed = System.currentTimeMillis();
        return c.value;
    }

    public List<TaskDto> getAllTask() {
        return cacheMap.entrySet().parallelStream()
                .filter(x -> x.getKey().getClazz().equals(Task.class)).map(x -> {
                    x.getValue().setLastAccessed(System.currentTimeMillis());
                    return (TaskDto) x.getValue().getValue();
                }).collect(Collectors.toList());
    }

    public List<GroupDto> getAllGroup() {
        return cacheMap.entrySet().stream()
                .filter(x -> x.getKey().getClazz().equals(Group.class)).map(x -> {
                    x.getValue().setLastAccessed(System.currentTimeMillis());
                    return (GroupDto) x.getValue().getValue();
                }).collect(Collectors.toList());
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private List<Assignment> getComposite(Long id) {
        List<Task> taskList = taskRepo.findByGroupsId(id);
        List<Assignment> assignments = new ArrayList<>(taskList);

        List<Group> groupList = groupRepo.findByGroupId(id);
        groupList.forEach(group -> group.setTasks(getComposite(group.getId())));

        assignments.addAll(groupList);

        return assignments;
    }

    @Data
    private class CacheObject {
        private long lastAccessed = System.currentTimeMillis();
        private Assignment value;

        public CacheObject(Assignment value) {
            this.value = value;
        }
    }

    @Data
    @AllArgsConstructor
    private class KeyObject {
        private Long key;
        private Class<? extends Assignment> clazz;
    }
}
