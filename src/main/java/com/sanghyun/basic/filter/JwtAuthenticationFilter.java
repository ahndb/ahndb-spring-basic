package com.sanghyun.basic.filter;

import java.io.IOException;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sanghyun.basic.provider.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// OncePerRequestFilter : 
//  - 해당 클래스를 필터 클래스로 지정하는 추상클래스
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  // 1. 클라이언트가 인증 토큰을 발급받음
  // 2. 인증 토근을 발급받은 후, 매 요청마다 인증 토큰을 request header의 Authorization 필드의 값으로 Bearer {토큰}을 포함하여 요청

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
        try {
          
          // 1. request 객체에서 token 가져오기
          String token = parseBearerToken(request);
          if (token == null) {
            return;
          }
          // 2. 토큰 검증
          String subject = jwtProvider.validation(token);
          if (subject == null) {
            return;
          }

        } catch (Exception exception) {
          exception.printStackTrace();
        }
    
  }
  // 1. request 객체에서 header를 가져옴
  // 2. 가져온 header에서 'Authorization' 필드를 검색
  // 3. 검색한 'Authorization' 값에서 'Bearer ' 이후의 값을 토큰으로 가져옴
  private String parseBearerToken(HttpServletRequest request) {
  
    // 1. request header의 'Authorization' 필드 값을 가져옴
    String authorization = request.getHeader("Authorization");
    // Authorization 필드의 값이 존재하는지 여부를 확인
    // null 여부, 빈 문자열 여부, 공백 문자열 여부
    boolean hasAuthorization = StringUtils.hasText(authorization);
    if (!hasAuthorization) return null;

    // 현재 요청이 Bearer Token Authentication이 맞는지 확인
    // 문자열의 시작이 'Bearer '로 시작하는지 여부 확인
    boolean isBearer = authorization.startsWith("Bearer ");
    if (!isBearer) return null;

    // 2. 'Authorization' 필드 값에서 'Bearer ' 이후의 값을 가져옴
    String token = authorization.substring(7);
    return token;
  }

}
