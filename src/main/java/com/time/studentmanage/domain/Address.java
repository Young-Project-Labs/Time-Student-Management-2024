package com.time.studentmanage.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String city;
    private String street;
    private String detail_address;

    //기본 생성자

    protected Address() {
    }

    public Address(String city, String street, String detail_address) {
        this.city = city;
        this.street = street;
        this.detail_address = detail_address;
    }
}
