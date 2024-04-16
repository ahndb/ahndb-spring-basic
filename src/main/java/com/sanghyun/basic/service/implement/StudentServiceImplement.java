package com.sanghyun.basic.service.implement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sanghyun.basic.dto.request.student.PatchStudentRequestDto;
import com.sanghyun.basic.dto.request.student.PostStudentRequestDto;
import com.sanghyun.basic.dto.request.student.SignInRequestDto;
import com.sanghyun.basic.entity.StudentEntity;
import com.sanghyun.basic.repository.StudentRepository;
import com.sanghyun.basic.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImplement implements StudentService {

  private final StudentRepository studentRepository;

  // 의존성을 외부에서 해주는게 가장 좋은데 읽기전용으로 되어있어 내부에서 써야함
  // PasswordEncoder 인터페이스 :
  // - Spring Security에서 제공해주는 비밀번호를 안전하게 관리하고 검증하도록 도움을 주는 인터페이스
  // - String 타입을 반환해주는 encode(평문의 패스워드) : 평문 패스워드를 암호화해서 반환
  // - Boolean 타입을 반환해주는 matches(평문패스워드를 첫번째 매개변수, 두번째 매개변수에 암호화된 패스워드) : 평문 패스워드와
  // 암호화된 패스워드가 같은지 비교 결과를 반환
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public ResponseEntity<String> postStudent(PostStudentRequestDto dto) {

    // ? 패스워드 암호화 작업
    String password = dto.getPassword(); // 패스워드를 불러옴
    String encodedPassword = passwordEncoder.encode(password); // 패스워드를 암호화함

    dto.setPassword(encodedPassword); // 암호화된 패스워드로 바꿔줌

    // CREATE (SQL : INSERT)
    // ? 1. Entity 클래스의 인스턴스 생성
    // ? 2. 생성한 인스턴스를 repository.save() 메서드로 저장
    StudentEntity studentEntity = new StudentEntity(dto);
    // studentEntity.setName(dto.getName());
    // studentEntity.setAge(dto.getAge());
    // studentEntity.setAddress(dto.getAddress());
    // studentEntity.setGraduation(dto.getGraduation());
    // studentEntity 에서 작업하면 생략 가능
    studentRepository.save(studentEntity);
    // ? save() : 저장 및 수정(덮어쓰기)
    return ResponseEntity.status(HttpStatus.CREATED).body("성공!");
  }

  // ^ 데이터베이스(RDBMS)의 Table 은 프로그래밍 언어에 class와 매핑됨
  // 객체지향픞로그래밍 언어에서 인스턴스 작업하면 테이블에 레코드(row)가 생김

  // 테이블로 접근 (UPDATE)
  // 전달받은 조건에 맞는 레코드를 검색 (WHERE)
  // 검색된 레코드의 원하는 값으로 변경 (SET)
  //////////////////////////////////////////////////
  @Override
  public ResponseEntity<String> patchStudent(PatchStudentRequestDto dto) {
    Integer studentNumber = dto.getStudentNumber();
    String address = dto.getAddress();

    // 0. Student 테이블에 해당하는 PK가 있는 레코드가 존재하는지 확인
    boolean isExistedStudent = studentRepository.existsById(studentNumber);
    if (!isExistedStudent)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 학생입니다.");

    // 1.클래스로 접근 (StudentRepository 사용)
    StudentEntity studentEntity = studentRepository.
    // 2. 전달받은 조건에 맞는 인스턴스를 검색
        findById(studentNumber).get();
    // 3. 검색된 인스턴스를 원하는 값으로 변경
    studentEntity.setAddress(address);
    // 4. 변경한 인스턴스를 데이터베이스에 저장
    // repository.save()는 레코드를 생성할 때 쓰이지만, 수정할 때도 동일하게 사용 됨
    studentRepository.save(studentEntity);

    return ResponseEntity.status(HttpStatus.OK).body("성공");
  }

  @Override
  public ResponseEntity<String> deleteStudent(Integer studentNumber) {

    // DELETE (SQ : DELETE)
    studentRepository.deleteById(studentNumber);

    return ResponseEntity.status(HttpStatus.OK).body("성공");
  }

  @Override
  public ResponseEntity<String> signIn(SignInRequestDto dto) {

    try {

      Integer studentNumber = dto.getStudentNumber();
      StudentEntity studentEntity = studentRepository.findByStudentNumber(studentNumber);

      if (studentEntity == null)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류");

      // 사용자가 입력한 패스워드와 암호화된 패스워드가 매치되는지 확인
      String password = dto.getPassword();
      String encodedPassword = studentEntity.getPassword();

      boolean isEqulPassword = passwordEncoder.matches(password, encodedPassword);
      if (!isEqulPassword)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 불일치");

    } catch (Exception exception) {
      exception.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류");
    }
    return ResponseEntity.status(HttpStatus.OK).body("성공!");
  }

}

// findBy
// findByName
// findByAge
// findByAddress

// And
// findByNameAndAge

// 비교
// findByNameAndAgeGreaterThan