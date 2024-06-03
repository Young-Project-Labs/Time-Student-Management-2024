package com.time.studentmanage.domain.enums;

public enum SearchType {

    CONTENT("내용"),
    TEACHER_NAME("선생님 이름"),
    STUDENT_NAME("학생 이름"),
    SCHOOL_NAME("학교 이름"),
    PARENT_NAME("부모님 이름"),
    EMAIL("이메일"),
    CLASS_NAME("학급 이름"),
    CLASS_INFO("학급 정보");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}