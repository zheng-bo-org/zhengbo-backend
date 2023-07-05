package org.zhengbo.backend.cache;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.zhengbo.backend.cache.prefixs.UserWebAccessToken;
import org.zhengbo.backend.utils.JSON;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCache implements Cache {
    private final Logger log = LoggerFactory.getLogger(RedisCache.class);
    private final HashMap<Class<? extends Prefix>, String> clzAndPrefixMappings = new HashMap<>(16){{
        put(UserWebAccessToken.class, "user_web_access_token:");
    }};

    private final RedisTemplate<String, String> redisTemplate;

    private String getPrefix(Class<? extends Prefix> clz) {
        String prefix = clzAndPrefixMappings.get(clz);
        if (prefix == null) {
            log.error("No such prefix found for the clz. The clz is {}", clz.getName());
            throw new RuntimeException("No such prefix found.");
        }

        return prefix;
    }

    private String generateKey(Class<? extends Prefix> prefix, String key) {
        String thePrefix = getPrefix(prefix);
        return thePrefix + key;
    }

    @Override
    public <T> void setJson(Class<? extends Prefix> prefix, String key, T value, long expireAtInMs) {
        String theKey = generateKey(prefix, key);
        var json = JSON.toJson(value);
        redisTemplate.opsForValue().set(theKey, json);
        redisTemplate.expire(theKey, expireAtInMs, TimeUnit.MICROSECONDS);
    }

    @Override
    public <T> Optional<T> getJson(Class<? extends Prefix> prefix, String key, Class<T> clz) {
        String theKey = generateKey(prefix, key);
        String value = redisTemplate.opsForValue().get(theKey);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(JSON.fromJson(value, clz));
    }

    @Override
    public void removeKey(Class<? extends Prefix> prefix, String key) {
        String theKey = generateKey(prefix, key);
        redisTemplate.opsForValue().getAndDelete(theKey);
    }

    @Override
    public boolean setValueIfAbsent(String key, String val) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, val));
    }

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void removeValue(String key) {
        redisTemplate.opsForValue().getAndDelete(key);
    }
}
