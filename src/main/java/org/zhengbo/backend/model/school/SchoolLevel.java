package org.zhengbo.backend.model.school;

public enum SchoolLevel {
    PRIMARY("小学"),
    JUNIOR_HIGH_SCHOOL("初中"),
    HIGH_SCHOOL("高中");

    public final String level;
    SchoolLevel(String level) {
        this.level = level;
    }
}
