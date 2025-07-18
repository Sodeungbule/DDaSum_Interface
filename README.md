# 🏡 DDaSum - 빈집 예약/관리/결제 백엔드

## ✨ 프로젝트 소개

DDaSum은 사용자 인증, 빈집 관리, 예약, 리뷰, 결제(Iamport 연동) 등 다양한 도메인을 지원하는 Spring Boot 기반 백엔드 프로젝트입니다.

- 🔒 **JWT 인증** & 이메일 인증(코드 발송/검증, Redis 활용)
- 🏠 **빈집 관리** (등록/조회/수정/삭제/검색)
- 📅 **예약** (예약/조회/취소)
- ⭐ **리뷰** (작성/수정/삭제/조회)
- 💳 **결제** (Iamport API 연동)
- 🛡️ **권한/로깅** (커스텀 어노테이션)
- 📖 **API 문서** (Swagger UI)

---

## 🗂️ 프로젝트 구조

```
src/main/java/com/ddasum/
├── DDaSumApplication.java
├── config/           # 환경설정 (EnvConfig 등)
├── core/             # 공통 유틸, 보안, 예외, 로깅, 인터셉터, 상수, 인증 등
└── domain/
    ├── user/         # 회원 (entity, repository, service, dto, controller)
    ├── vacanthouse/  # 빈집 (entity, repository, service, dto, controller)
    ├── reservation/  # 예약 (entity, repository, service, dto, controller)
    ├── review/       # 리뷰 (entity, repository, service, dto, controller)
    ├── payment/      # 결제 (entity, repository, service, dto, controller)
    ├── region/       # 지역 (entity, repository, service, dto, controller)
    ├── regioncurrency/ # 지역화폐 (entity, repository, service, dto, controller)
    └── community/    # 커뮤니티(게시판, 댓글 등)
```

---

## ⚙️ 기술 스택

- ☕ **Spring Boot 3.2.5**
- 🐘 **Java 17**
- 🗄️ **JPA (Hibernate)**
- 🐬 **MariaDB**
- 🟥 **Redis** (이메일 인증 코드 저장)
- 🛡️ **JWT** (io.jsonwebtoken)
- 📝 **Swagger (springdoc-openapi)**
- 🔄 **ModelMapper**
- 💸 **Iamport 결제 API**
- 🦾 **Lombok**
- 🛠️ **Gradle**

---

## 🔑 환경 변수 및 설정

모든 민감 정보는 `.env` 파일 또는 시스템 환경 변수로 관리합니다.  
`src/main/resources/application.properties`는 환경 변수만 참조합니다.

### 📄 예시: `.env`
```
DB_URL=jdbc:mariadb://localhost:3306/ddasum
DB_USERNAME=root
DB_PASSWORD=1234

JWT_SECRET=your_jwt_secret
JWT_ACCESS_TOKEN_EXPIRATION=1800000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

UPLOAD_DIR=uploads

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

SERVER_PORT=8080

REDIS_HOST=localhost
REDIS_PORT=6379

IAMPORT_API_KEY=your_iamport_key
IAMPORT_API_SECRET=your_iamport_secret
```
> ⚠️ `.env` 파일은 `.gitignore`에 추가되어 Git에 커밋되지 않습니다.

---

## 🚀 실행 방법

1. **환경 변수 설정**  
   `.env` 파일을 프로젝트 루트에 생성하고 위 예시처럼 값 입력

2. **의존성 설치 및 빌드**
   ```bash
   ./gradlew build
   ```

3. **서버 실행**
   ```bash
   ./gradlew bootRun
   ```

4. **API 문서 접속**  
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🧪 테스트/개발/운영 환경

- `application-dev.properties` : 개발용 기본값
- `application-prod.properties` : 운영 환경(모든 값은 환경 변수로만 주입)

---

## 💡 기타

- 🧑‍💻 **Swagger UI**에서 JWT Authorize 버튼으로 토큰 인증 테스트 가능
- 🔄 **ModelMapper**로 DTO-Entity 변환 자동화
- 🛡️ **커스텀 어노테이션**으로 권한/로깅 처리
- 🚨 **에러/예외 처리** 및 API 응답 일관성 유지

---

## 🙋 문의/기여

- 🤝 Pull Request, Issue 환영합니다!
- 💌 질문은 이슈 또는 이메일로 주세요. 