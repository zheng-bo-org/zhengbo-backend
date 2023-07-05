package org.zhengbo.backend.cache;

import java.util.HashMap;
import java.util.Optional;

public interface Cache {
    interface Prefix {
    }

    <T> void setJson(Class<? extends Prefix> prefix, String key, T clz, long expireAtInMs);

    <T> Optional<T> getJson(Class<? extends Prefix> prefix, String key, Class<T> clz);

    void removeKey(Class<? extends Prefix> prefix, String key);

    boolean setValueIfAbsent(String key, String val);

    String getValue(String key);

    void removeValue(String key);
}
