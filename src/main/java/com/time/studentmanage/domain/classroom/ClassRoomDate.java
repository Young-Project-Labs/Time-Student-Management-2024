package com.time.studentmanage.domain.classroom;

import com.time.studentmanage.domain.record.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class ClassRoomDate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_room_date_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_day_id")
    private ClassDay classDay;

    // 연관 관계 편의 메서드 //
    public void addClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
        classRoom.getClassRoomDateList().add(this);
    }

    public void addClassDay(ClassDay classDay) {
        this.classDay = classDay;
        classDay.getClassRoomDateList().add(this);
    }
}
