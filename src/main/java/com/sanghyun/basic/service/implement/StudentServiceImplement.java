package com.sanghyun.basic.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sanghyun.basic.service.PostStudentRequestDto;
import com.sanghyun.basic.service.StudentService;

@Service
public class StudentServiceImplement implements StudentService {

  @Override
  public ResponseEntity<String> postStudent(PostStudentRequestDto dto) {
    return null
  }
  
}
