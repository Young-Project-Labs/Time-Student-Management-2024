package com.time.studentmanage.repository.classroom;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.member.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    List<ClassRoom> findAllByTeacherOrderByClassType(Teacher teacher);
}
