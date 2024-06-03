package com.time.studentmanage.domain.record;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    /**
     * 기간 별 조회를 확인하기 위한 필드 설정
     */
//    @CreatedDate
//    protected LocalDateTime createDate;
//
//    @LastModifiedDate
//    protected LocalDateTime modifiedDate;
//
//
//    public void setCreateDate(LocalDateTime createDate) {
//        this.createDate = createDate;
//    }
//
//    public void setModifiedDate(LocalDateTime modifiedDate) {
//        this.modifiedDate = modifiedDate;
//    }
}
