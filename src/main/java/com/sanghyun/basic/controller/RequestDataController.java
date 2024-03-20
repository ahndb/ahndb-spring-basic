package com.sanghyun.basic.controller;

import org.springframework.web.bind.annotation.GetMapping;
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
    @RequestParam (name="userId") String userId,
    @RequestParam (name="userName") String userName
  ) {
    return "사용자 아이디:" + userId + "/ 사용자 이름:" + userName;
  }











}
