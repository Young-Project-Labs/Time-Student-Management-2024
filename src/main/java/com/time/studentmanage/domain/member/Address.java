package com.time.studentmanage.domain.member;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter @Setter
@ToString
public class Address {
    @NotBlank
    private String streetAddress;
    @NotBlank
    private String detailAddress;
    @NotBlank
    private String zipCode;

    //기본 생성자

    protected Address() {
    }

    public Address(String streetAddress, String detailAddress, String zipCode) {
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
    }
}
