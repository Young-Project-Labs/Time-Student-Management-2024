package com.time.studentmanage.service;

import com.time.studentmanage.repository.RecordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

    private final RecordsRepository recordsRepository;

}
