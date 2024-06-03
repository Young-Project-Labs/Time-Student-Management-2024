package com.time.studentmanage.service;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.dto.classroom.*;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.classroom.ClassRoomRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void deleteClassRoom(Long id) {
        Optional<ClassRoom> classRoomOp = classRoomRepository.findById(id);

        if (!classRoomOp.isPresent()) {
            throw new IllegalArgumentException("학급 정보가 존재하지 않습니다.");
        }

        classRoomRepository.delete(classRoomOp.get());
    }

    public Page<ClassRoomRespDto> getAllTeacherClassRoom(Teacher teacher, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ClassRoomRespDto> classRoomPagingResult = classRoomRepository.findAllPaging(teacher, pageable);

        return classRoomPagingResult;
    }

    public ClassRoomBasicInfoDto getBasicClassRoomInfo(Long id) {
        Optional<ClassRoom> classRoomOp = classRoomRepository.findById(id);

        if (!classRoomOp.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 학급 정보입니다.");
        }

        ClassRoom classRoomPs = classRoomOp.get();

        return new ClassRoomBasicInfoDto(classRoomPs.getId(), classRoomPs.getName(), classRoomPs.getClassInfo(), classRoomPs.getClassType());
    }

    public void updateClassRoomBasicInfo(Long id, ClassRoomBasicInfoDto classRoomBasicInfoDto) {
        Optional<ClassRoom> classRoomOp = classRoomRepository.findById(id);

        if (!classRoomOp.isPresent()) {
            throw new IllegalArgumentException("학급 정보가 존재하지 않습니다.");
        }

        ClassRoom classRoomPS = classRoomOp.get();

        classRoomPS.changeClassRoomName(classRoomBasicInfoDto.getName());
        classRoomPS.changeClassInfo(classRoomBasicInfoDto.getClassInfo());
        classRoomPS.changeClassType(classRoomBasicInfoDto.getClassType());
    }

    public List<ClassStudentRespDto> getClassStudentList(Long id) {
        Optional<ClassRoom> classRoomOp = classRoomRepository.findById(id);

        if (!classRoomOp.isPresent()) {
            throw new IllegalArgumentException("학급 정보가 존재하지 않습니다.");
        }

        ClassRoom classRoomPS = classRoomOp.get();

        List<ClassStudentRespDto> resultList = classRoomPS.getStudentList().stream()
                .map(s -> new ClassStudentRespDto(s.getId(), s.getSchoolName(), s.getGrade(), s.getName(), s.getPhoneNumber()))
                .collect(Collectors.toList());

        return resultList;
    }

    public ClassRoom findById(Long classId) {
        return classRoomRepository.findById(classId).get();
    }

    public Page<ClassRoomRespDto> getPageUpdateResult(Long teacherId, SearchType searchType, String content, int page) {

        Pageable pageable = PageRequest.of(page, 10);
        ClassRoomSearchReqDto classRoomSearchReqDto = new ClassRoomSearchReqDto(searchType, content, teacherId, page);
        Page<ClassRoomRespDto> pageResult = classRoomRepository.findAllBySearchEngine(classRoomSearchReqDto, pageable);
        return pageResult;
    }
}
