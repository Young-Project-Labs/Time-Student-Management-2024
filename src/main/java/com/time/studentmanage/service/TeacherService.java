package com.time.studentmanage.service;

import com.time.studentmanage.domain.dto.teacher.TeacherRespDto;
import com.time.studentmanage.domain.dto.teacher.TeacherSaveReqDto;
import com.time.studentmanage.domain.dto.teacher.TeacherUpdateReqDto;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 선생_정보_조회
    public TeacherRespDto getTeacherInfo(Long teacherId){
        Optional<Teacher> teacherOP = teacherRepository.findById(teacherId);

        if (!teacherOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 ID입니다.");
        }

        //존재 시 teacher 리턴.
        return new TeacherRespDto(teacherOP.get());
    }
    
    // 선생_정보_수정
    public TeacherRespDto updateTeacherInfo(Long id, TeacherUpdateReqDto updateReqDto) {
        Optional<Teacher> teacherOP = teacherRepository.findById(id);
        if (!teacherOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 ID입니다.");
        }

        teacherOP.get().changeEntity(updateReqDto.getId(), updateReqDto.toEntity());

        Teacher resultTeacher = teacherRepository.save(teacherOP.get());

        return new TeacherRespDto(resultTeacher);
    }

    //선생_목록(관리자 페이지)
    public List<TeacherRespDto> getTeacherAllList() {
        List<Teacher> teacherList = teacherRepository.findAll();

        //Teacher -> TeacherRespDto
        List<TeacherRespDto> teacherRespDtoList = teacherList.stream()
                .map(teacher -> new TeacherRespDto(teacher.getId(),teacher.getName(), teacher.getPhoneNumber(), teacher.getEmail(), teacher.getPosition(), teacher.getMemberType(), teacher.getGender()))
                .collect(Collectors.toList());

        return teacherRespDtoList;
    }

    //선생_등록(관리자 페이지)
    public void createTeacher(TeacherSaveReqDto teacherSaveReqDto) {
        //중복 등록 방지 (성함 & 이메일)
        Optional<Teacher> teacherOP = teacherRepository.findByNameAndEmail(teacherSaveReqDto.getName(), teacherSaveReqDto.getEmail());
        if (teacherOP.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 선생님입니다.");
        }

        teacherRepository.save(teacherSaveReqDto.toEntity(bCryptPasswordEncoder));
    }

    //선생_이메일_중복_체크
    public void checkEmailDuplication(String email) {
        Optional<Teacher> teacherOP = teacherRepository.findByEmail(email);
        String regexp = "^[\\w.-]+@time\\.com$";
        // 정규표현식 만족 X (@time.com)
        if (!email.matches(regexp)) {
            throw new IllegalArgumentException("아이디 형식을 확인해주세요.");
        } else{
            //존재 -> 중복
            if (teacherOP.isPresent()) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }
        }

    }


}
