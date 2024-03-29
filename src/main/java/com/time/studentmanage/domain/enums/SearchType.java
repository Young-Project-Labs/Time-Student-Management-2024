package com.time.studentmanage.domain.enums;

public enum SearchType {
    CONTENT("내용"), TEACHER_NAME("선생님");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
