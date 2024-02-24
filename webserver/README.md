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
  - refactor 반드시 필요

### 요구사항 6 - stylesheet 적용
- Content-type 을 전달하는 content의 확장자에 따라 결정하도록 구현.

# 설계
![image](https://github.com/JuneParkCode/java-mvc/assets/81505228/f58bfa1c-c4de-425a-90e0-417bec7c4d2a)

## 분리
- 가능한 책임 나눠서 분리.
### Webserver
#### core
- webserver 구동에 필요한 핵심 로직
- 전체 webserver 의 흐름을 담고 있음.
  - client--request---===socket===\[----parse--request---routing---handle---response---]==socket==---> client
#### http
- webserver 내에서 사용하는 http utility
- core 구동에서 http 관련 작업과 관련되어있음.
### app (server code)
- 온전히 server logic 을 작성하는 부분
  - Webserver 라는 서버 프레임 워크 기반에서 라우팅 경로별 서버 로직을 작성함.
 
# 남은 문제
## Router injection
- 해당 부분에 아직 의존성이 남아있음 (`app` 내의 코드(server code)작성 만으로 주입 불가)
  - `webserver.Webserver` 코드 직접 수정 필요
## Test
- Http Request Parsing 하는 부분 일부만 테스트되어있음.
  - Normal case 에 대해서만 가정하고 작성하여 error case 에서는 서버가 터질 가능성 높음.
- socket i/o 가 포함된 부분에 대한 테스트 방법에 대해서 학습 필요
  - 이후 테스트 가능한 부분에 대해서 추가로 테스트
- Router 모듈 테스트 필요
## Response
- Response 관련 코드에서 주먹구구식으로 작성된 부분이 많음.

## Refactor
- Utility 관련 코드의 복잡도가 높음.
- Exception filtering 하는 부분 추상화 / 분리 필요
- http 모듈 전체적으로 refactor 필요
- `Handler` 가 `Function<HttpRequest, HttpResponse>` 인데, request 를 직접 받지 않고, 필요한 param 을 받는 형태 (Spring처럼) 변경되어야함.
  
# 학습 필요
- Java Reflection
- Java Stream / Reader
- Nullable -> Optional 활용 방법
- Java enum
