package com.sanghyun.basic.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice : RestController에서 발생하는 특정 상황들에 대해 처리하는 클래스 지정
@RestControllerAdvice
public class CustomExceptionHandler {

  // @ExceptionHandler : 지정한 예외에 대해 직접 컨트롤 할 수 있도록 하는 어노테이션
  @ExceptionHandler(value = { MethodArgumentNotValidException.class })
  public ResponseEntity<String> CustomException(MethodArgumentNotValidException exception) {

    return ResponseEntity.status(400).body("필수 데이터를 입력하지 않았습니다.");
  }

}
