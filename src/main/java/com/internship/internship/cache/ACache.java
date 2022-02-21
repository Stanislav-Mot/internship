package com.internship.internship.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class ACache<K,T> {
    @Value("${application-cache-timeInterval}")
    private long timeInterval;
    @Value("${application-cache-timeToLive}")
    private long timeToLive;
    @Value("${application-cache-max}")
    private int max;

    private HashMap<K, T> cacheMap;

    protected class ACacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public String value;

        protected ACacheObject(String value) {
            this.value = value;
        }
    }

    public ACache() {
        this.timeToLive = timeToLive * 2000;

        cacheMap = new HashMap<K, T>(max);

        if (timeToLive > 0 && timeInterval > 0) {

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(timeInterval * 1000);
                    } catch (InterruptedException ex) {
                    }

                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    // PUT method
    public void put(K key, T value) {
        synchronized (cacheMap) {
            cacheMap.put(key, value);
        }
    }

    // GET method
    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (cacheMap) {
            ACacheObject c = (ACacheObject) cacheMap.get(key);

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return (T) c.value;
            }
        }
    }

    // REMOVE method
    public void remove(String key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    // Get Cache Objects Size()
    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    // CLEANUP method
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<String> deleteKey = null;

        synchronized (cacheMap) {
            Iterator<?> itr = cacheMap.entrySet().iterator();

            deleteKey = new ArrayList<String>((cacheMap.size() / 2) + 1);
            ACacheObject c = null;

            while (itr.hasNext()) {
                String key = (String) itr.next();
                c = (ACacheObject) ((Map.Entry<?, ?>) itr).getValue();
                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        for (String key : deleteKey) {
            synchronized (cacheMap) {
                cacheMap.remove(key);
            }

            Thread.yield();
        }
    }
}
