package com.time.studentmanage.domain.enums;

public enum AttendanceStatus {
    Y("재원"), N("휴원");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
