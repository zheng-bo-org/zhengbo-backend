package org.zhengbo.backend.model.user;

public enum Gender {
    FEMALE("男"),
    MALE("女"),
    UNKNOWN("未知");


    public final String name;
    Gender(String name) {
        this.name = name;
    }
}
