package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.exception.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentSearchResult<T> { // 컬렉션을 감싸서 다른 필드들이 필요할 때 추가하여 컬렉션만 반환하지 않고 필드 속성을 추가할 수 있다.
    private T data;
    private ErrorResult errorResult;
}
