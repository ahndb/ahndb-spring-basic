package com.sanghyun.basic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 엔터티는 Getter와 AllArgsConstructor 만 있는게 정석
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {
  private String name;
  private Integer age;
  private String address;
  private Boolean graduation;
}
