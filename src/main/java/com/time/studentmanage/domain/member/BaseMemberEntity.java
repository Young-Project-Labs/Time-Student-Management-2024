package com.time.studentmanage.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMemberEntity {

    @CreatedDate
    @Column(updatable = false) // 생성일은 수정 불가
    private LocalDateTime create_time;

    @LastModifiedDate
    private LocalDateTime modified_time;
}
