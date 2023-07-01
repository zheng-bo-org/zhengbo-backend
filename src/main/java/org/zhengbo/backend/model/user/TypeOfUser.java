package org.zhengbo.backend.model.user;

public enum TypeOfUser {
    STUDENT("学生"),
    TEACHER("老师"),
    PARENT("家长");
    public final String typeName;
    TypeOfUser(String typeName) {
        this.typeName = typeName;
    }
}
