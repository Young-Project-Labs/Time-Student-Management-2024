package com.time.studentmanage;

import com.time.studentmanage.config.TestDataInit;
import com.time.studentmanage.config.dataInit;
import com.time.studentmanage.repository.classroom.ClassRoomRepository;
import com.time.studentmanage.repository.record.RecordRepository;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import com.time.studentmanage.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing
public class StudentmanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentmanageApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(StudentService studentService, StudentRepository studentRepository, TeacherRepository teacherRepository, RecordRepository recordRepository, ClassRoomRepository classRoomRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new TestDataInit(studentService, studentRepository, teacherRepository, recordRepository, classRoomRepository, bCryptPasswordEncoder);
    }

    //    @Bean
//    @Profile("prod")
    public dataInit init(TeacherRepository teacherRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new dataInit(teacherRepository, bCryptPasswordEncoder);
    }
}
