package com.sanghyun.basic.dto.request.student;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostStudentRequestDto {

  @NotBlank
  private String name;

  @NotNull
  @Min(0)
  private Integer age;
  
  @NotBlank
  private String address;

  @NotNull
  private Boolean graduation;
}
