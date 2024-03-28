package com.sanghyun.basic.service;

import org.springframework.http.ResponseEntity;

import com.sanghyun.basic.dto.request.student.PatchStudentRequestDto;
import com.sanghyun.basic.dto.request.student.PostStudentRequestDto;

public interface StudentService {
  ResponseEntity<String> postStudent(PostStudentRequestDto dto);
  ResponseEntity<String> patchStudent(PatchStudentRequestDto dto);
  ResponseEntity<String> deleteStudent(Integer studentNumber);
}

// @@ 이런 작업을 하겠다. 