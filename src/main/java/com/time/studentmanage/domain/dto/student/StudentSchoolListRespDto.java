package com.time.studentmanage.domain.dto.student;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudentSchoolListRespDto {
    List<String> elementarySchools = new ArrayList<>();
    List<String> middleSchools = new ArrayList<>();
    List<String> highSchools = new ArrayList<>();
}
