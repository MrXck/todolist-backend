package com.todo.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheUtils<K, V> {

    private final Cache<K, CacheObject<V>> cache;

    public CacheUtils() {
        cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();
    }

    public CacheUtils(long duration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(duration, timeUnit).build();
    }

    public void put(K key, V value, long duration, TimeUnit timeUnit) {
        CacheObject<V> cacheObject = new CacheObject<>(value, duration, timeUnit);
        cache.put(key, cacheObject);
    }

    public void put(K key, V value) {
        CacheObject<V> cacheObject = new CacheObject<>(value, 30, TimeUnit.MINUTES);
        cache.put(key, cacheObject);
    }

    public V get(K key) {
        CacheObject<V> cacheObject = cache.getIfPresent(key);
        if (cacheObject == null) {
            return null;
        }

        if (System.currentTimeMillis() > cacheObject.getExpireTime()) {
            cache.invalidate(key);
            return null;
        }

        return cacheObject.getValue();
    }

    public void remove(K key) {
        cache.invalidate(key);
    }

    private static class CacheObject<V> {

        private final V value;

        private final long expireTime;

        CacheObject(V value, long duration, TimeUnit timeUnit) {
            this.value = value;
            expireTime = System.currentTimeMillis() + timeUnit.toMillis(duration);
        }

        public V getValue() {
            return value;
        }

        public long getExpireTime() {
            return expireTime;
        }
    }
}
