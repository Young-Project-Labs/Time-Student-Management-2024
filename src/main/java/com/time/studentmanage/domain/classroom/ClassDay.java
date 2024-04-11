package com.time.studentmanage.domain.classroom;

import com.time.studentmanage.domain.enums.ClassDateType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class ClassDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_day_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ClassDateType classDateType; // MON, TUE, WED, THU, FRI, SAT

    @OneToMany(mappedBy = "classDay")
    private List<ClassRoomDate> classRoomDateList;
}
