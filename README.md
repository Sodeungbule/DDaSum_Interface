# 🏡 DDaSum - Server Interface

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

## 🗂️ 프로젝트 구조 - 레이어드 아키텍처 형식

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


## 🔑 환경 변수 및 설정

모든 민감 정보는 `.env` 파일 또는 시스템 환경 변수로 관리합니다.  
`src/main/resources/application.properties`는 환경 변수만 참조합니다.

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