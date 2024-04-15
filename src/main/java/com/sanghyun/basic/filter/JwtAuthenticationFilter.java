package com.sanghyun.basic.filter;

import java.io.IOException;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
            filterChain.doFilter(request, response);
            return;
          }
          // 2. 토큰 검증
          String subject = jwtProvider.validation(token);
          if (subject == null) {
            filterChain.doFilter(request, response);
            return;
          }
          
          // 3. principle의 대한 정보를 controller로 전달하기 위해 context에 담기
          
          // 3-1. 인증된 사용자라는 의미의 AbstractAuthenticationToken 객체를 생성
          // (사용자의 이름, 비밀번호, 사용자권한)을 매개변수로 넣어준다.
          AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(subject, null, AuthorityUtils.NO_AUTHORITIES);

          // 3-2. 인증 요청에 대한 세부정보를 등록 / 웹 인증 정보를 해당 리퀘스트에 등록
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // 3-3. 빈 security context 생성
          SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

          // 3-4. 생성한 빈 security context에 생성한 인증 토큰을 설정
          securityContext.setAuthentication(authenticationToken);

          // 3-5. 생성한 security context를 사용할 수 있도록 등록
          SecurityContextHolder.setContext(securityContext);

        } catch (Exception exception) {
          exception.printStackTrace();
        }
    
        // 4. 다음 필터에 request 객체와 response 객체를 전달

        filterChain.doFilter(request, response);
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
