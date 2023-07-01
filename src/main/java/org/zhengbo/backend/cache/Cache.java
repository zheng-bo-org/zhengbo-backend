package org.zhengbo.backend.cache;

import java.util.HashMap;

public interface Cache {
    interface Prefix {
    }

    <T> void setJson(Class<? extends Prefix> prefix, String key, T clz, long expireAtInMs);

    <T> T getJson(Class<? extends Prefix> prefix, String key, Class<T> clz);

    void removeKey(Class<? extends Prefix> prefix, String key);
}
