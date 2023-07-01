package org.zhengbo.backend.cache;

public interface Cache {
    <T> void setJson(String key, T clz, long expireAtInMs);
    <T> T getJson(String key, Class<T> clz);

    void removeKey(String key);
}
