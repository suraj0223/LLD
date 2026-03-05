package com.inmemory_key_value;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryCache<K, V> {
    private final ConcurrentHashMap<K, CacheValue<V>> map;
    private final ScheduledExecutorService cleaner;

    public InMemoryCache(long cleanupIntervalMillis) {
        this.map = new ConcurrentHashMap<>();
        this.cleaner = Executors.newScheduledThreadPool(10);
        startCleanupTask(cleanupIntervalMillis);
    }

    private void startCleanupTask(long intervalMillis) {
        cleaner.scheduleAtFixedRate(() -> {
            try {
                for (var entry : map.entrySet()) {
                    if (entry.getValue().isExpired()) {
                        map.remove(entry.getKey());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error occurred during cleanup: " + e.getMessage());
            }
        }, intervalMillis, intervalMillis, TimeUnit.MILLISECONDS);
    }
    
    public void put(K key, V value, long expiration) {
        map.put(key, new CacheValue<>(value, expiration));
    }

    public V get(K key) {
        return map.get(key).getValue();
    }

    public void delete(K key) {
        map.remove(key);
    }
    
    public void shutdown() {
        cleaner.shutdown();
    }
    
}