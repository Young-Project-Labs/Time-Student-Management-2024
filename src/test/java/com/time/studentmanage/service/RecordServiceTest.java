package com.time.studentmanage.service;

import com.time.studentmanage.repository.RecordsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    RecordsRepository recordsRepository;

    @InjectMocks
    RecordService recordService;

    @Test
    void 학교별_모든_피드백_조회() {
        //given

        //when
        //then
    }

    @Test
    void RecordServiceTest() {
        //given

        //when
        //then
    }
}