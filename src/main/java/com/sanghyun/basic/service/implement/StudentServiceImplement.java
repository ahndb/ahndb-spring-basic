package com.sanghyun.basic.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sanghyun.basic.dto.request.student.PostStudentRequestDto;
import com.sanghyun.basic.entity.StudentEntity;
import com.sanghyun.basic.repository.StudentRepository;
import com.sanghyun.basic.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImplement implements StudentService {

  private final StudentRepository studentRepository;

  @Override
  public ResponseEntity<String> postStudent(PostStudentRequestDto dto) {

    // CREATE (SQL : INSERT)
    //? 1. Entity 클래스의 인스턴스 생성
    //? 2. 생성한 인스턴스를 repository.save() 메서드로 저장
    StudentEntity studentEntity = new StudentEntity(dto);
    // studentEntity.setName(dto.getName());
    // studentEntity.setAge(dto.getAge());
    // studentEntity.setAddress(dto.getAddress());
    // studentEntity.setGraduation(dto.getGraduation());
    // studentEntity 에서 작업하면 생략 가능
    studentRepository.save(studentEntity);
    //? save() : 저장 및 수정(덮어쓰기)
    return ResponseEntity.status(HttpStatus.CREATED).body("성공!");
  }

}

// 데이터베이스의 Table 은 프로그래밍 언어에 class와 매핑됨
// 인스턴스 작업하면 테이블에 레코드가 생김
