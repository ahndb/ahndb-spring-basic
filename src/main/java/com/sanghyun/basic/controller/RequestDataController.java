package com.sanghyun.basic.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
// HTTP *localhost:4000/request-data-/**
@RequestMapping("/request-data")
public class RequestDataController {

  // @RequestParam() :
  // - GET, DELETE 처럼 URL로 데이터를 전송하는 메서드에서 Query Sting 으로 지정된 데이터를 가져오기 위헤 사용

  // HTTP GET localhost:4000/request-data/request-param
  @GetMapping("/request-param")
  // http://localhost:4000/request-data/request-param?userId=qwer&userName=gildong
  public String getRequestParam(
      @RequestParam(name = "userId") String userId,
      @RequestParam(name = "userName", required = false) String userName, // requried=false 지정시 필수가 아니게 됨
      @RequestParam() int userAge) {
    return "사용자 아이디:" + userId + " / 사용자 이름:" + userName + " / 사용자 나이:" + userAge;
  }

  // @PathVariable() :
  // - 모든 HTTP 메서드에서 URL의 특정 패턴에 따라서 데이터를 추출하는 방식

  // HTTP DELETE localhost:4000/request-data/path-variable
  @DeleteMapping("/path-variable/{age}/{name}")
  // http://localhost:4000/request-data/path-variable/10
  public String deletePathVariable(
      @PathVariable("age") Integer age,
      @PathVariable(name="name", required=false) String name) {
    return "사용자 나이:" + age + " / 사용자 이름:" + name;
  }

  // HTTP PATCH localhost:4000/request-data/patch/gildong/update
  @PatchMapping("/patch/{userName}/update")

  public String patchUpdate(
      @PathVariable("userName") String userName) {
    return "사용자 이름:" + userName;
  }

  // ! *****주의
  // URL 패턴으로 데이터를 받아오는 방식을 썻을 때
  // 겹치는 패턴이 존재하는지 잘 확인해야 함
  @GetMapping("/{value}/get")
  public String getPathVariable1(
      @PathVariable("value") String value) {
    return "getPathVariable1";
  }

  @GetMapping("/get/{value}")
  public String getPathVariable2(
      @PathVariable("value") String value) {
    return "getPathVariable2";
  }

}
