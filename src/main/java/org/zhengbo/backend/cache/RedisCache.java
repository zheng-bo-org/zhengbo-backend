package org.zhengbo.backend.cache;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.zhengbo.backend.utils.JSON;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCache implements Cache {
    private final Logger log = LoggerFactory.getLogger(RedisCache.class);

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public <T> void setJson(String key, T value, long expireAtInMs) {
        var json = JSON.toJson(value);
        redisTemplate.opsForValue().set(key, json);
        redisTemplate.expire(key, expireAtInMs, TimeUnit.MICROSECONDS);
    }

    @Override
    public <T> T getJson(String key, Class<T> clz) {
        String value = redisTemplate.opsForValue().get(key);
        return JSON.fromJson(value, clz);
    }

    @Override
    public void removeKey(String key) {
        redisTemplate.opsForValue().getAndDelete(key);
    }
}
