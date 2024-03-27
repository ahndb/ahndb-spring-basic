package com.sanghyun.basic.service;

import org.springframework.http.ResponseEntity;

import com.sanghyun.basic.dto.request.student.PostStudentRequestDto;

public interface StudentService {
  ResponseEntity<String> postStudent(PostStudentRequestDto dto);
}
