package com.internship.internship.service;

import com.internship.internship.model.Assignment;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {
    //time in seconds
    private final long timeIntervalForCleanup = 5;
    private final long timeToLiveInCache = 10;

    private final Map<Long, ACacheObject> cacheMap;

    public CacheService() {
        cacheMap = new ConcurrentHashMap<>();

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

    public void put(Long key, Assignment value) {
        cacheMap.put(key, new ACacheObject(value));
    }

    public Assignment get(Long key) {
        ACacheObject c = cacheMap.get(key);
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

    }

    @Data
    private class ACacheObject {
        private long lastAccessed = System.currentTimeMillis();
        private Assignment value;

        public ACacheObject(Assignment value) {
            this.value = value;
        }
    }
}
