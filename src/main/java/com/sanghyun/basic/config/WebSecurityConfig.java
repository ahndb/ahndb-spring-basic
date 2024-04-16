package com.sanghyun.basic.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sanghyun.basic.filter.JwtAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Configuration : Spring boot 기본설정 지정 및 변경
@Configuration
// @Configurable :
// - 생성자나 '메서드'가 호출시에 Spring bean 등록을 자동화(제어 역전)하는 어노테이션
@Configurable
// @EnableWebSecurity :
// - Web Security 설정을 지원하도록 하는 어노테이션
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  // ? @Bean :
  // - Spring bean으로 등록하는 어노테이션
  // - @Component를 사용하지 못할 때 사용
  // - class에서도 사용가능
  // - @Autowired의 목적이 아닐때 사용
  @Bean
  protected SecurityFilterChain configure(HttpSecurity security) throws Exception {
    // ? class::method :
    // - 메소드 참조, 특정 클래스의 메서드를 참조할 때 사용
    // - 일반적으로 매개변수로 메서드를 전달할 때 사용됨
    security
        // basic authentication 미사용 지정
        .httpBasic(HttpBasicConfigurer::disable)
        // ? session :
        // - 웹 애플리케이션에서 사용자의 대한 정보 및 상태를 유지하기 위한 기술
        // - 서버측에서 사용자 정보 및 상태를 저장하는 방법
        // - REST API 서버에서는 사용자 정보 및 상태를 클라이언트가 유지하기 때문에 session을 생성하지 않음
        // ? cookie :
        // - 웹 애플리케이션에서 사용자의 대한 정보 및 상태를 유지하기 위한 기술
        // - 클라이언트측에서 사용자 정보 및 상태를 저장하는 방법
        // ? session과 cookie의 차이
        // * - 저장위치 : cookie는 클라이언트, session은 서버
        // * - 보안 : session이 보안 수준이 높음
        // * - 수명 : cookie는 지정한 기간동안 지속적으로 유지, session은 연결이 끊기면 파기됨
        // * - 용도 : cookie에는 간단한 데이터(id, token)를 저장, session에는 민감한 데이터 (개인정보)를 저장
        // ? chche :
        // - 데이터나 값을 미리 복사해두고 저장하는 임시 공간
        // - 사용자의 접근을 조금 더 빠르게 할 수 있도록 함
        // - 시스템 성능 향상
        // - 하드웨어 캐시 : cpu cache, disk cache
        // - 소프트웨어 캐시 : web cache, database cache
        // - 네트워크 캐시 : CDN
        .sessionManagement(sessionManagement -> sessionManagement
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 사용자의 정보를 저장하지 않을거기때문에 세션을 생성하지 않음
        // ? CSRF (Cross-Site Request Forgery) :
        // 크로스 사이트 요청 위조
        // * */ - 클라이언트(사용자)가 자신의 의도와는 무관한 공격행위를 하는 것
        // ? SQL Injection :
        // - 공격자가 데이터베이스의 쿼리문을 직접 조작하여 데이터를 탈취하는 공격
        // ? XSS (Cross-Site Scripting) :
        // - 공격자가 웹 브라우저에 악성 스크립트를 작성하여 실행
        .csrf(CsrfConfigurer::disable)
        // Spring Security 사용 이후에는 CORS 정책을 Security Filter Chain에 등록
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        // 요청 URL의 패턴에 따라 리소스 접근 허용 범위를 지정
        // 1. 인증되지 않은 사용자도 접근을 허용
        // 2. 인증된 사용자 중 특정 권한을 가지고 있는 사용자만 접근을 허용
        // 3. 인증된 사용자는 모두 접근을 허용
        .authorizeHttpRequests(request -> request
            // 특정 URL 패턴에 대한 요청은 인증되지 않은 사용자도 접근을 허용
            .requestMatchers(HttpMethod.GET, "/auth/**").permitAll() // 1.
            // 특정 URL 패턴에 대한 요청은 지정한 권한을 가지고 있는 사용자만 접근을 허용
            // .requestMatchers("/student", "/student/**").hasRole("STUDENT") // 2.
            .requestMatchers("/student", "/student/**").permitAll()
            // 인증된 사용자는 모두 접근을 허용함
            .anyRequest().authenticated() // 3.
        )
        // # 인증 과정 중에 발생한 예외 처리
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new FailedAuthenticationEntryPoint()));

    // # 우리가 생성한 jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 이전에
    // 등록
    security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return security.build();
  }

  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedMethod("*");
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;

  }

}

// 인증 실패 처리를 위한 커스텀 예외 처리(AuthenticationEntryPoint 인터페이스 구현)
class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    authException.printStackTrace();

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write("{ \"message\":\"인증에 실패했습니다.\" }");

  }

}