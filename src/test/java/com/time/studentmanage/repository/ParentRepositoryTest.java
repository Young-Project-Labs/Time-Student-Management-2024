package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Parent;
import com.time.studentmanage.domain.member.Student;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.DynamicUpdate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@Transactional
@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)

class ParentRepositoryTest {
    @Autowired
    ParentRepository parentRepository;

    @Order(1)
    @Test
    void 부모_회원_가입_테스트() {
        Parent parent = createParent();

        //when
        parentRepository.save(parent);

        //then
        Optional<Parent> findParent = parentRepository.findById(parent.getId());
        if (findParent.isPresent()) {
            log.info("findParent={}", findParent.get().getName());
            assertThat(parent.getName()).isEqualTo(findParent.get().getName());

        } else {
            log.error("찾는 부모회원이 존재하지 않습니다.");
        }


    }


    @Order(2)
    @Test
    public void 부모_정보_수정_테스트() throws Exception {
        //given
        Parent parent = createParent();
        Parent saveParent = parentRepository.save(parent);


        //when
        Optional<Parent> findParent = parentRepository.findById(saveParent.getId());
        if (findParent.isPresent()) {
            findParent.get().setName("이름을 수정합니다.");
            parentRepository.flush();
        } else {
            log.error("회원을 찾을 수 없습니다.");
        }

        //then
        Optional<Parent> validationParent = parentRepository.findById(findParent.get().getId());
        //flush 후에 변경된 엔티티를 조회하여 이름이 맞는 지 검증.
        assertThat(validationParent.get().getName()).isEqualTo(findParent.get().getName());
        log.info("flush 후 수정 후 이름 ={}",validationParent.get().getName());

    }
    @Order(3)
    @Test
    public void 부모_계정_탈퇴_테스트() throws Exception {
        //given
        Parent parent = createParent();
        Parent parent2 = createParent();
        Parent parent3 = createParent();
        Parent saveParent = parentRepository.save(parent);
        Parent saveParent2 = parentRepository.save(parent2);
        Parent saveParent3 = parentRepository.save(parent3);
        //when
        parentRepository.delete(saveParent);

        //then
        assertThat(parentRepository.findAll().size()).isEqualTo(3);


    }
    // 부모 생성 메서드
    private Parent createParent() {
        //given
        Student student1 = new Student("철수", "cs@time.com", "1234", "010-1111-2222", "용호초등학교", ClassType.ELEMENTARY, 1, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "현대 아파트", "102-1201"), AttendanceStatus.Y);
        Parent parent = new Parent("철수엄마", "010-1234-4567", MemberType.PARENT, GenderType.FEMALE);
        // 연관관계 메서드 사용
        parent.addStudent(student1);
        return parent;
    }


}