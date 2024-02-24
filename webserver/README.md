# Java Webserver
- Java 웹 프로그래밍 Next step 책을 기반으로 web server 를 제작합니다.
- https://github.com/slipp/web-application-server (*원본 레포)
- 전체적인 커리큘럼은 따라가되, 깊은 이해를 위해 직접 구현한 부분이 추가로 존재합니다.

## 요구사항 (Ch03)
### 요구사항 0 (개별 프로젝트로 만들기 위함)
- Maven -> Gradle
- Dependency 버전 업그레이드
  - JUnit 4 -> JUnit 5
- Webserver 기본 뼈대 재구현
  - 기본으로 제공되는 템플릿 참고하여 재구현
  - Java 이해를 위함
  - 일부 Util 의 경우 기존 코드 활용

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
- `/` path 에 대해서 `/webapp` 에 대한 static file 을 serving할 수 있도록 구현
- `if / switch` 로 구성된 routing 이 아닌, Controller -> Router 주입으로 구현
- 아주 간단한 버전의 `HTTPRequestParser`구성

### 요구사항 2 - get 방식으로 회원가입
- `GET /user/create`, URL Query parsing

### 요구사항 3 - post 방식으로 회원가입
- `POST /user/create`, `application/x-www-form-urlencoded` 형태의 body 읽기

### 요구사항 4 - redirect 방식으로 이동
- response 시 status code 에 따라 적절하게 reseponse

### 요구사항 5 - cookie
- Set-Cookie 관련 구현
  - 이후 refactor 반드시 필요

### 요구사항 6 - stylesheet 적용
- Content-type 을 전달하는 content의 확장자에 따라 결정하도록 구현.