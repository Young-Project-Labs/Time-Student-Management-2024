package com.time.studentmanage.domain.enums;

public enum ProviderType {
    GENERAL("일반회원"), KAKAO("카카오회원");

    private final String description;

    ProviderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
