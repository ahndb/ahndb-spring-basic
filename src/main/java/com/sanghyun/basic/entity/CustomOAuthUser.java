package com.sanghyun.basic.entity;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;

// OAuth2.0 인증에 사용되는 유저에 대한 정보를 담는 인스터스를 생성하는 클래스
// OAuth2User 인터페이스를 구현하여 작성해야함
@AllArgsConstructor
public class CustomOAuthUser implements OAuth2User {

  // 완전 캡슐화가되게 만들거
  private String name;
  private Map<String, Object> attributes;
  private Collection<? extends GrantedAuthority> authorities;

  @Override // 추가적인 속성
  public Map<String, Object> getAttributes() {
    return this.attributes;
  }

  @Override // 권한
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override //princple
  public String getName() {
    return this.name;
  }

}
