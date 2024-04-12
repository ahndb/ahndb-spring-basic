package com.sanghyun.basic.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// JWT : 
//  - Json Web Token, RFC 7519 표준에 정의된 json 형식의 문자열을 포함하고 있는 토큰
//  - 인증 및 인가에 사용
//  - 암호화가 되어 있어 클라이언트와 서버 간의 안전한 데이터 교환을 할 수 있음
//  - 헤더, 페이로드, 서명으로 구성됨
//?  - 헤더 : 토큰의 유형(일반적으로 jwt)과 암호화 알고리즘이 포함되어 있음
//?  - 페이로드 : 클라이언트 혹은 서버가 상대방에게 전달할 데이터가 포함되어 있음(작성자, 접근주체의 정보, 작성시간, 만료시간 등)
//?  - 서명 : 헤더와 페이로드를 합쳐서 인코딩하고 지정한 비밀키로 암호화한 데이터
@Component
public class JwtProvider {

  //? JWT 암호화에 사용되는 비밀키는 보안 관리가 되어야 함
  //? 코드에 직접적으로 비밀키를 작성하는 것은 보안상 좋지 않음
  
  //! 해결책
  //# 1. application.properties / application.yaml에 등록
  //  - application.properties 혹은 application.yaml에 비밀키를 작성
  //  - @Value()를 이용하여 데이터를 가져옴
  //  import org.springframework.beans.factory.annotation.Value;
  //  - 주의사항 : application.properties / application.yaml을 .gitignore에 등록해야함
  @Value("${jwt.secret-key}")
  private String secretKey;
  
    //# 2. 시스템의 환경변수로 등록하여 사용 (1번보다 보안상 더 높은 형태임)
    //  - OS 자체의 시스템 환경 변수에 비밀키(변수와 값)를 작성
    //  - Spring에서 Environment 객체를 이용하여 값을 가져옴

    //# 3. 외부 데이터 관리 도구(솔루션)를 사용
    //  - 해당 서버가 아닌 타 서버에 등록된 Vault 도구를 사용하여 비밀키를 관리
    //  - OS 부팅시에 Vault 서버에 접근하여 비밀키를 가져와서 사용하는 방법
    //  - 매 부팅시 다른 비밀키를 제공해줌

  // JWT 생성
  public String create(String principle) {
    // 만료시간
    Date expiredDate = Date.from(Instant.now().plus(4, ChronoUnit.HOURS));
    // 비밀키 생성
    // 이렇게 명시하면 안됨, 환경변수로 등록하거나, 서버에 보관해서 쓰거나 등 다른데 저장해서 사용해야 함
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    // JWT 생성
    String jwt = Jwts.builder() // 생성을 위한 빌더 작업 (상자준비)
        // 빌더 후 서명 작업을 해야함
        // 서명(서명에 사용할 비밀키, 서명에 사용할 암호화 알고리즘)
        .signWith(key, SignatureAlgorithm.HS256)
        // 페이로드 내용 (물건담기)
        // "sub(작성자)"값 
        .setSubject(principle)
        // 생성시간
        .setIssuedAt(new Date())
        // 만료시간
        .setExpiration(expiredDate)
        // 위 내용을 압축 (인코딩 함)
        .compact(); // (닫고 잠구기)

    return jwt;

  };

  // (물건 꺼내기)
  public String validation(String jwt) {

    // jwt 검증 결과로 나타나는 페이로드가 저장될 변수
    Claims claims = null;
    // 비밀키 생성 (사용했던 키 가져오기)
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    //(열기 시도)
    try {
      // 비밀키로 jwt 복호화 작업
      claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jwt)
        .getBody();
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    }

    return claims.getSubject();
  };

}
