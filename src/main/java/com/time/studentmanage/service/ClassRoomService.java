package com.time.studentmanage.service;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.dto.classroom.ClassRoomInfoDto;
import com.time.studentmanage.domain.dto.classroom.ClassSaveReqDto;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.classroom.ClassRoomRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final TeacherRepository teacherRepository;

    public ClassRoom saveClassRoom(ClassSaveReqDto classSaveReqDto) {
        Optional<Teacher> teacherOp = teacherRepository.findById(classSaveReqDto.getTeacherId());

        // 선생님 아이디가 유효한지 검사
        if (!teacherOp.isPresent()) {
            throw new IllegalArgumentException("잘못된 선생님 정보입니다.");
        }

        Teacher teacherPS = teacherOp.get();

        ClassRoom classRoom = ClassRoom.builder()
                .name(classSaveReqDto.getName())
                .classInfo(classSaveReqDto.getClassInfo())
                .teacher(teacherPS)
                .classType(classSaveReqDto.getClassType())
                .build();

        ClassRoom savedClass = classRoomRepository.save(classRoom);

        return savedClass;
    }

    public void updateClassRoomName(Long id, String name) {
        Optional<ClassRoom> classRoomOp = classRoomRepository.findById(id);

        if (!classRoomOp.isPresent()) {
            throw new IllegalArgumentException("학급 정보가 존재하지 않습니다.");
        }

        ClassRoom classRoomPS = classRoomOp.get();

        classRoomPS.changeClassRoomName(name);
    }

    public void deleteClassRoom(Long id) {
        Optional<ClassRoom> classRoomOp = classRoomRepository.findById(id);

        if (!classRoomOp.isPresent()) {
            throw new IllegalArgumentException("학급 정보가 존재하지 않습니다.");
        }

        classRoomRepository.delete(classRoomOp.get());
    }

    // TODO: 적절한 dto로 매핑하기
    public List<ClassRoomInfoDto> getAllTeacherClassRoom(Teacher teacher) {
        List<ClassRoom> classRoomList = classRoomRepository.findAllByTeacherOrderByClassType(teacher);

        List<ClassRoomInfoDto> resultDtoList = classRoomList.stream()
                .map(c -> new ClassRoomInfoDto(c.getId(), c.getName(), c.getClassInfo(), c.getClassType(), c.getStudentList().size()))
                .collect(Collectors.toList());
        return resultDtoList;
    }
}
