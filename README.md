# spring-vote-21st
ceos back-end 21st vote project
<br>
<br>


---
# 1. 프백 합동과제 협업 환경 세팅
### (1) git convention은 어떻게 세팅했는가?
**1. commit & pr** : commit 메세지와 pr 제목을 동일하게 작성합니다.
pr 메세지의 경우 전달사항을 간략하게 적습니다.

**2. issue convention** : pr을 올릴 시 어떤 이슈인지 태그를 사용합니다. (feat/fix/docs 등)

**3. branch 전략** : 현재 과제는 브랜치 1개만 이용했지만 프로젝트에서는 feature, dev, master 3개 브랜치를 이용하여 배포 및 기능 구현 브랜치를 분리하였습니다.
<br>
### (2) repository 세팅
**- 프론트/백 repository**
: organization에서 back-end, front-end 레포지토리 개발 공간을 마련했습니다.

<br>


### (3) 개발 스타일 맞추기
**1. 폴더 구조**
- **도메인 중심 설계 기반**
- **domain / global 폴더 구분** : domain 패키지 아래에 auth, jwt, user 등 기능별 하위 도메인을 분리했습니다.
 global 패키지에는 공통적인 사용되는 설정 클래스(config), 응답 및 예외 처리(common), 유틸 함수(util) 등을 분리했습니다.
- **계층형 아키텍쳐 구조** : 각 도메인(auth, jwt, user 등) 하위에 controller, service, repository 등의 폴더를 두었습니다.
<br>

**2. 파일명, 변수명 convention**

**- 기술 스택 선정**
| 언어 | Java 17 |
| --- | --- |
| framework | spring Boot 3.x |
| 빌드 도구 | Gradle |
| DB | MySQL |
| ORM | Spring data jpa + Hibernate |
| API 문서화 | Swagger |
| 테스트 | JUnit5 + Mockito + Swagger |
| 로그 | Spring Boot Logging (로그 포맷 통일 필요) |
| 배포 | Docker + AWS EC2 |
| 형상 관리 | Git + github |
| 인증/인가 | Spring Security + JWT |


<br>

**- git repo setup**
- **Repository 이름** : DearDream / VoteDream
- **branch 전략**
    - **main** : 운영용
    - **dev** : 개발 통합
    - feature/* : 기능 단위 브랜치 → 뒤에는 feature/#1 과 같이 순서를 진행시킴
    
    → **feature/기능 단위** 브랜치로 하기로 결정
    
    예시) [feature/attendance-fix], [feature/attendance-pagination], [feature/celebrationMsg-admin] 
    
- **PR 템플릿 생성**
    
    ```java
    ## 작업 개요
    - 작업 내용 요약
    
    ## 주요 변경 사항
    - 상세 변경내용 bulltet 형식으로
    
    ## 참고 사항
    - 테스트 방법/ 관련 이슈 등
    ```
    
- **Issue 템플릿**
    - `FEAT` →  새로운 기능 추가
    - `REFACTOR`  → 코드 리팩토링
    - `TEST`  → 테스트 코드, 리팩토링 테스트 코드 추가
    - `FIX`  → 버그 수정
    - `DOCS`  → 문서 수정
    - `BAD` → Write bad code that needs to be improved (리팩토링 필요하다)
    - `CHORE`  → 기타 변경 사항

- pr 작성 시 아래 방식으로 작성

→ [FIX] ~~

→ [CHORE] sorting the numbers

→ [DOCS] api 명세서 작성

- pr 작성 할 때 라벨 선택


<br>


**3. - 프로젝트 구조 예시**
<br>
```
com.example.project
├── domain
│   ├── user
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── dto
|   |   ├── converter
|   |   ├── exception
│   │   └── entity
│   └── ... (다른 도메인)
│
├── global
│   ├── config            # 전역 설정 (Security, CORS 등)
│   ├── exception         # 전역 예외 처리
│   ├── util              # 유틸리티 클래스
│   ├── common            # 공통 모듈 (ResponseDto 등)
│   ├── security          # JWT, 필터, 인증/인가 설정 등
│   └── advice            # 예외 핸들링 @ControllerAdvice 등
│
└── Application.java      # main 클래스

```

---

### **(4) response format, exception 관리**

**4-1. Response Format 설계 방식**
- 프로젝트 전반에서 모든 api 응답은 공통 포맷(ApiResponseObject<T>)으로 응답합니다.
- 기본 구조
  ```
  {
  "isSuccess": true,
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "result": { ... }
  }
  ```
- 이를 위해 아래와 같은 DTO를 사용합니다.<br>

**(1) ApiResponseObject<T>**

**(2) ApiResponseJwtDto** : 로그인 시 JWT 토큰 응답 전용

**(3) JwtDto** : accessToken, refreshToken 포함한 내부 DTO


**4-2. 예외 처리 방식 요약**
- 전역 예외 처리(GlobalExceptionHandler)
 -> @RestControllerAdvice + @ExceptionHandler로 통합 처리합니다.
- 예시 :

```
{
  "isSuccess": false,
  "code": "VALIDATION_ERROR",
  "message": "비밀번호는 8자 이상이어야 합니다.",
  "result": null
}
```

<br>

# 2. 로그인/회원가입 기능 구현
**- DTO 구조 예시**
- 회원가입 요청 DTO : SignUpRequestDto
```
{
  "loginId": "string",
  "password": "string",
  "email": "string",
  "part": "FRONTEND",
  "username": "string",
  "team": "DEARDREAM"
}
```

- 로그인 요청 DTO : LoginRequestDto
```
{
  "loginId": "string",
  "password": "string"
}
```

- 로그인 응답 DTO : ApiResponseJwtDto
```
{
  "isSuccess": true,
  "code": "SUCCESS",
  "message": "로그인 성공",
  "result": {
    "accessToken": "string",
    "refreshToken": "string"
  }
}
```
<br>

---


# 3. 수동 배포
- AWS EC2 기반 수동 배포.
- http://52.78.76.206:8080/swagger-ui/index.html#/ 
- 프론트엔드와 연결 예시
![image](https://github.com/user-attachments/assets/a7b6a07c-0e29-4052-b24d-f495b261ae9c)
<br>

![image](https://github.com/user-attachments/assets/0bd79ea4-1503-43c2-ab1f-5293e558c64a)

