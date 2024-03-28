//! spring init
//*   ->  application.properties(server.port 변경) #8080 -> 4000
//?         --> src/main/resources (위치)
//*   ->  controller 생성 (@RestController, @RequestMapping)
//*   ->  각 HTTP 요청에 따른 method 생성 (@Get, Post, Put, Patch, DeleteMapping)
//?         --> 브라우저의 스크립트로 요청이 넘어왔을 때 같은 출처가 아니라면 CORS 정책으로 막힘
//?         --> CorsConfig 클래스를 생성하여 Cors 정책 변경 (@Configuration, @WebMvConfigurer)
//?         --> CorsRegistry 객체를 설정 (addMapping : URL 패턴 지정, allowedMethods: HTTP method 지정, allowedOrigins: 출처 지정)
//*   ->  각 HTTP 요청에서 데이터를 받음 (@RequestParam, @PathVariable, @RequestBody)
//*   ->  데이터를 받을 때  필수 체크 @RequestParam, @PathVariable (required 속성 사용)
//*   ->  @RequestBody는 validation 라이브러리를 활용 (build.gradle 의존성 추가)
//*   ->  @RequestBody의 JSON 데이터를 DTO로 받음
//*   ->  @RequestBody에서 유효성 검사 어노테이션으로 각 속성마다 검사 (@NotNull, @NotEmpty, @NotBlank, ...)
//*   ->  validation을 사용해서 유효성 검사 시에 유효하지 않으면 spring boot가 자동으로 response body를 생성해서 응답
//?         --> ExceptionHandler 클래스로 validation 관련 예외 직접 처리(@RestControllerAdvice, @ExceptionHandler)
//*   ->  각 HTTP 응답을 직접 제어하기 위해 ResponseEntity 클래스를 사용 (status code, body, header, ... 제어)

//^ Repository , Entity 는 table 명으로 한다
//^ 나머지는 명칭이 주는 의미보다 모듈에 대한 의미로 봐야함

//! 컨트롤 - 서비스 - 레포지토리 - 데이터베이스

// post, patch, put은 Request body가 가능하다.
// get, delete는 Request body가 불가능하다.