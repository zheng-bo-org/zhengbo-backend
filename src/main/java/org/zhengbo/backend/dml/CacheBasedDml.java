package org.zhengbo.backend.dml;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.zhengbo.backend.cache.Cache;

@Component
@Configuration
@RequiredArgsConstructor
public class CacheBasedDml implements Dml {
    private final Cache cache;

    private String generateKey(String id, Class<? extends LockType> lockType) {
        return lockType.getName() + ":" + id;
    }

    @Override
    public void lock(String id, Class<? extends LockType> lockType) {
        String key = generateKey(id, lockType);
        while (cache.setValueIfAbsent(key, key) == false);
    }

    @Override
    public void unlock(String id, Class<? extends LockType> lockType) {
        String key = generateKey(id, lockType);
        cache.removeValue(key);
    }
}
