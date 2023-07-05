package org.zhengbo.backend.dml;


public interface Dml {
    interface LockType {}

    void lock(String id, Class<? extends LockType> lockType);

    void unlock(String id, Class<? extends LockType> lockType);
}
