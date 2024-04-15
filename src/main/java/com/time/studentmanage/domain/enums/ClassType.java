package com.time.studentmanage.domain.enums;

public enum ClassType {
    ELEMENTARY("초등"), MIDDLE("중등"), HIGH("고등");

    private final String description;

    ClassType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
