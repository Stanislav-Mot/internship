package com.internship.internship.service;

import com.internship.internship.dto.GroupDto;
import com.internship.internship.dto.TaskDto;
import com.internship.internship.model.Assignment;
import com.internship.internship.repository.GroupRepo;
import com.internship.internship.repository.TaskRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CacheService {
    //time in seconds
    private final long timeIntervalForCleanup = 5;
    private final long timeToLiveInCache = 10;

    private final Map<KeyObject, CacheObject> cacheMap;

    private final TaskRepo taskRepo;
    private final GroupRepo groupRepo;

    public CacheService(TaskRepo taskRepo, GroupRepo groupRepo) {
        this.taskRepo = taskRepo;
        this.groupRepo = groupRepo;

        cacheMap = new ConcurrentHashMap<>();
        addAll();

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(timeIntervalForCleanup * 1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                cleanup();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void put(Long key, String clazz, Assignment value) {
        cacheMap.put(new KeyObject(key, clazz), new CacheObject(value));
    }

    public Assignment get(Long key) {
        CacheObject c = cacheMap.get(key);
        c.lastAccessed = System.currentTimeMillis();
        return c.value;
    }

    public void remove(String key) {
        cacheMap.remove(key);
    }

    public int size() {
        return cacheMap.size();
    }

    private void cleanup() {
        long now = System.currentTimeMillis();

        cacheMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null && (now > ((timeToLiveInCache * 1000) + entry.getValue().lastAccessed)))
                .map(Map.Entry::getValue)
                .forEach(cacheMap::remove);
    }

    private void addAll() {
//        taskService.getAll().forEach(x -> put(x.getId(), "task", x));
//        groupService.getAll().forEach(x -> put(x.getId(), "group", x));
    }

    public Assignment getTask(Long id) {
        CacheObject c = cacheMap.get(new KeyObject(id, "task"));
        c.lastAccessed = System.currentTimeMillis();
        return c.value;
//        TaskDto taskDto;
//        if ((taskDto = (TaskDto) cacheService.getTask(id)) == null) {
//            taskDto = mapper.convertToDto(taskRepo.getTaskById(id));
//            cacheService.put(taskDto.getId(), "task", taskDto);
//        }
    }

    public Assignment getGroup(Long id) {
        CacheObject c = cacheMap.get(new KeyObject(id, "group"));
        c.lastAccessed = System.currentTimeMillis();
        return c.value;
    }

    public List<TaskDto> getAllTask() {
        return cacheMap.entrySet().stream()
                .filter(x -> x.getKey().getClazz().equals("task")).map(x -> {
                    x.getValue().setLastAccessed(System.currentTimeMillis());
                    return (TaskDto)x.getValue().getValue();
                }).collect(Collectors.toList());

    }

    public List<GroupDto> getAllGroup() {
        return cacheMap.entrySet().stream()
                .filter(x -> x.getKey().getClazz().equals("group")).map(x -> {
                    x.getValue().setLastAccessed(System.currentTimeMillis());
                    return (GroupDto)x.getValue().getValue();
                }).collect(Collectors.toList());
        //        return groupRepo.getAll()
//                .stream().map(mapper::convertToDto)
//                .collect(Collectors.toList());
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
        private String clazz;
    }
}
