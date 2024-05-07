package com.time.studentmanage.domain.enums;

public enum SearchType {
    CONTENT("내용"), TEACHER_NAME("선생님"), STUDENT_NAME("학생 이름"), SCHOOL_NAME("학교 이름"), PARENT_NAME("부모님 이름"), EMAIL("이메일");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}