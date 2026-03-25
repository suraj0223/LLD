package com.inmemory_key_value;

public class CacheValue<T> {
    private final T cacheValue;
    private final long expiryTime;

    public CacheValue(T cacheValue, long expiryTime) {
        this.cacheValue = cacheValue;
        this.expiryTime = expiryTime;
    }

    public T getValue() {
        return this.cacheValue;
    }

    public boolean isExpired() {
        return expiryTime != -1 && System.currentTimeMillis() > expiryTime;
    }
    
}