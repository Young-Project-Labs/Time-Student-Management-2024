package com.time.studentmanage.domain.enums;

public enum MailSearchType {
    USERID("아이디"), PASSWORD("패스워드");

    private final String description;

    MailSearchType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
