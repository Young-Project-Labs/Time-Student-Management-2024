//package com.time.studentmanage.domain.member;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn
//public class Member extends BaseMemberEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "parent_id")
//    private Long id;
//
//    private String userId;
//    private String password;
//
//    // TODO: 공통 엔티티를 묶어서 하나의 레포지터리로 관리하기 위한 고민을 해보았음 추후 더 나은 방향이면 리팩토링 예정 230306
//}
