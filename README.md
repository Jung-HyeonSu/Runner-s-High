# 🏃 Runner's High

> 러닝 코스를 그리고, 나누고, 달리세요.
> 러닝 커뮤니티 및 운동 기록 관리 웹 플랫폼

---

## 목차

- [프로젝트 소개](#프로젝트-소개)
- [기술 스택](#기술-스택)
- [아키텍처](#아키텍처)
- [시작하기](#시작하기)

---

## 프로젝트 소개

Runner's High는 러너들이 지도 위에 직접 코스를 그리고 공유하며, 러닝 기록을 관리하고 메달을 획득할 수 있는 모바일 웹 플랫폼입니다.

---

## 기술 스택

### Backend

| 기술 | 버전 | 선택 이유 |
|------|------|-----------|
| **Java** | 17 | LTS 버전. `record` 타입으로 Command/DTO 간결하게 작성 |
| **Spring Boot** | 4.0.2 | 자동 설정·내장 서버로 빠른 개발 환경 구성 |
| **Spring Security** | - | 인증/인가를 필터 체인으로 일관되게 관리. URL별 접근 제어 선언적 처리 |
| **Spring Data JPA** | - | Repository 인터페이스 추상화로 도메인이 DB 구현에 의존하지 않도록 분리 |
| **Spring Validation** | - | `@Valid` + Bean Validation으로 입력값 검증을 Controller에 선언적으로 위임 |
| **JJWT** | 0.12.6 | Access/Refresh Token 발급·검증. HttpOnly 쿠키로 XSS 방어 |
| **Lombok** | - | 반복적인 보일러플레이트 제거 (`@Getter`, `@RequiredArgsConstructor` 등) |
| **MySQL** | - | 운영 환경 관계형 DB |
| **H2** | - | 테스트 환경 인메모리 DB. 별도 설치 없이 테스트 실행 가능 |

### Frontend

| 기술 | 선택 이유 |
|------|-----------|
| **React 18** | 컴포넌트 기반으로 탭별 UI 독립적 관리. hooks로 상태와 사이드이펙트 분리 |
| **Tailwind CSS** | 유틸리티 클래스로 별도 CSS 파일 없이 다크 테마 일관 적용 |
| **Kakao Maps API** | 국내 지도 서비스 중 웨이포인트·폴리라인·CustomOverlay API 안정성 우수 |
| **Babel Standalone** | 빌드 도구 없이 브라우저에서 JSX 직접 트랜스파일. 단일 HTML 파일로 배포 가능 |

---

## 아키텍처

### 전체 구조

```
┌─────────────────────────────────────────────────────────┐
│                        Browser                          │
│                                                         │
│   ┌─────────────────────────────────────────────────┐   │
│   │            React SPA (index.html)               │   │
│   │                                                 │   │
│   │  ┌──────────┐ ┌───────────┐ ┌────────────────┐  │   │
│   │  │  MapTab  │ │DrawCourse │ │Ranking/Profile │  │   │
│   │  └──────────┘ └───────────┘ └────────────────┘  │   │
│   │              ┌──────────────────┐                │   │
│   │              │  Kakao Maps API  │                │   │
│   │              └──────────────────┘                │   │
│   └────────────────────┬────────────────────────────┘   │
│                        │ HTTP + HttpOnly Cookie (JWT)    │
└────────────────────────┼────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                   Spring Boot Server                    │
│                                                         │
│  ┌─────────────┐   ┌─────────────┐   ┌──────────────┐  │
│  │presentation │──▶│ application │──▶│    domain    │  │
│  │  Controller │   │   Service   │   │Entity / Repo │  │
│  │  Req/ResDTO │   │   Command   │   │  Interface   │  │
│  └─────────────┘   └─────────────┘   └──────┬───────┘  │
│                                             │           │
│  ┌──────────────────────────────────────────▼───────┐   │
│  │                  infrastructure                  │   │
│  │      JwtFilter  ·  SecurityConfig  ·  JpaRepo   │   │
│  └──────────────────────────────────────────────────┘   │
└────────────────────────┬────────────────────────────────┘
                         │
                ┌────────▼─────────┐
                │     MySQL DB     │
                │                  │
                │  members         │
                │  courses         │
                │  course_waypoints│
                │  runs            │
                └──────────────────┘
```

### 레이어 설계 원칙

```
presentation ──▶ application ──▶ domain ◀── infrastructure
```

- **단방향 의존**: 각 레이어는 하위 레이어에만 의존한다
- **domain 독립성**: domain은 어떤 레이어에도 의존하지 않는다
- **DIP 적용**: infrastructure가 domain의 Repository 인터페이스를 구현해 의존성을 역전시킨다

### 인증 흐름

```
Client                        Server
  │                              │
  │──── POST /auth/login ────────▶│
  │                              │  이메일/비밀번호 검증
  │                              │  Access Token  발급 (15분)
  │                              │  Refresh Token 발급 (7일)
  │◀─── 200 + Set-Cookie ────────│  HttpOnly 쿠키로 전달
  │                              │
  │──── GET /api/** ─────────────▶│
  │     Cookie: access_token     │  JwtAuthenticationFilter
  │                              │  토큰 검증 → SecurityContext 저장
  │◀─── 200 ─────────────────────│
  │                              │
  │──── POST /auth/refresh ──────▶│  Refresh Token 검증
  │                              │  새 Access Token 발급
  │◀─── 200 + Set-Cookie ────────│
```

- Access Token은 **HttpOnly 쿠키**로 전달해 JavaScript에서 접근 불가 (XSS 방어)
- Refresh Token으로 Access Token 재발급, 만료 시 재로그인 유도

---

### 설계 결정 사항

- **`CourseWaypoint`를 `@Embeddable`로 분리**: 웨이포인트는 코스 없이 독립적으로 존재하지 않으므로 별도 엔티티가 아닌 값 객체로 설계
- **`Email`을 Embedded 타입으로 분리**: 이메일 형식 검증 로직을 도메인 내부에 캡슐화
- **`Run.courseId`를 nullable로 설계**: 코스 없이 자유롭게 러닝을 기록할 수 있도록 유연성 확보
- **`totalDistance`를 Member에 보관**: 러닝 기록 조회 없이 프로필·메달 계산에 바로 사용 가능하도록 비정규화

---

## 시작하기

### 사전 요구사항

- Java 17+
- MySQL 8.0+

### 환경 변수 설정

`application-dev.properties` 파일을 생성하고 아래 값을 채웁니다. (이 파일은 gitignore 처리됨)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/runners_high?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
spring.datasource.username=
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=    # 32자 이상 랜덤 문자열
```

### 실행

```bash
# 빌드
./gradlew clean build -x test

# 실행
./gradlew bootRun

# 테스트
./gradlew test
```

서버 실행 후 `http://localhost:8080` 접속
