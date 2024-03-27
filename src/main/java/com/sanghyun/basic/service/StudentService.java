package com.sanghyun.basic.service;

import org.springframework.http.ResponseEntity;

public interface StudentService {
  ResponseEntity<String> postStudent(PostStudentRequestDto dto)
}
