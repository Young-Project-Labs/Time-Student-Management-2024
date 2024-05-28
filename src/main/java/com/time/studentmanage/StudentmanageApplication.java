package com.time.studentmanage;

import com.time.studentmanage.config.TestDataInit;
import com.time.studentmanage.config.dataInit;
import com.time.studentmanage.repository.record.RecordRepository;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
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
    public TestDataInit testDataInit(StudentRepository studentRepository, TeacherRepository teacherRepository, RecordRepository recordRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new TestDataInit(studentRepository, teacherRepository, recordRepository, bCryptPasswordEncoder);
    }

    @Bean
    @Profile("prod")
    public dataInit init(TeacherRepository teacherRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new dataInit(teacherRepository, bCryptPasswordEncoder);
    }
}
