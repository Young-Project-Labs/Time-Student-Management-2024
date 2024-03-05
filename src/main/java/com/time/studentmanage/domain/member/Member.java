package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.enums.MemberType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;
    private String name;
    private String phone_number;
    private LocalDateTime create_time;

}
