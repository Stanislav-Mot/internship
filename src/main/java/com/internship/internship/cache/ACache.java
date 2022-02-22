package com.internship.internship.cache;

import com.internship.internship.model.Assignment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class ACache {
    //The interval is specified in seconds
    private final long timeInterval = 5;
    private final long timeToLive = 10;
    private final int max = 10;

    private HashMap<Long, ACacheObject> cacheMap;

    protected class ACacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public Assignment value;

        protected ACacheObject(Assignment value) {
            this.value = value;
        }
    }

    public ACache() {
        cacheMap = new HashMap<>(max);

        if (timeToLive > 0 && timeInterval > 0) {

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(timeInterval * 1000);
                    } catch (InterruptedException ex) {
                    }
                    cleanup();
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }

    public void put(Long key, Assignment value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new ACacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public Assignment get(Long key) {
        synchronized (cacheMap) {
            ACacheObject c;
            c =  cacheMap.get(key);
            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(String key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<Long> deleteKey;

        synchronized (cacheMap) {
            Iterator<Map.Entry<Long, ACacheObject>> itr = cacheMap.entrySet().iterator();
            deleteKey = new ArrayList<>((cacheMap.size() / 2) + 1);

            Long key;
            ACacheObject c;

            while (itr.hasNext()) {
                Map.Entry<Long, ACacheObject> next = itr.next();
                key = next.getKey();
                c = next.getValue();
                if (c != null && (now > ((timeToLive * 1000) + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        for (Long key : deleteKey) {
            synchronized (cacheMap) {
                cacheMap.remove(key);
            }
            Thread.yield();
        }
    }
}
