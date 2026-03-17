# Runner's High 프로젝트 컨텍스트

## 프로젝트 개요

Runner's High - 러닝 커뮤니티 및 운동 기록 관리 플랫폼

## 기술 스택

- **Backend**: Java 17, Spring Boot 3.x, Gradle
- **Database**: JPA / Spring Data JPA, H2 (테스트), MySQL (운영)
- **Security**: Spring Security
- **테스트**: JUnit 5, Mockito, AssertJ

## 프로젝트 구조

```
src/main/java/com/runnershigh/
├── presentation/           # Controller, Request/Response DTO
│   ├── common/             # ApiResponse, GlobalExceptionHandler
│   └── {domain}/           # 도메인별 Controller, DTO
├── application/            # Service, Command, Exception
│   ├── common/exception/   # BusinessException, ErrorCode
│   └── {domain}/           # 도메인별 Service, Command DTO
├── domain/                 # Entity, Repository Interface
│   ├── common/             # BaseEntity
│   └── {domain}/entity/    # 도메인 엔티티
│       repository/         # Repository 인터페이스
└── infrastructure/         # Repository 구현체, 외부 연동, Config
    ├── config/             # Spring 설정
    └── persistence/        # JPA Repository 구현체
```

## 빌드 & 실행

```bash
# 빌드
./gradlew clean build -x test

# 테스트
./gradlew test

# 실행
./gradlew bootRun
```

## If you finished code update

Update relevant comments or documentation if needed.

---

# 코드 컨벤션

## Util 관련
- 날짜 관련: DateUtil (날짜 계산, 비교 등)
- JSON 관련: ObjectConvertUtil
- 랜덤: RandomUtil (랜덤 키 생성, 리스트에서 랜덤한 요소 하나 가져올 때 등)
- 정규식 관련: RegexUtil

## 레이어별 역할

### presentation 레이어
- Controller: HTTP 요청/응답 처리, `@Valid` 유효성 검증 위임
- Request DTO: 외부 입력값 수신 → `toCommand()`로 Command 변환
- Response DTO: Service 결과를 클라이언트에 반환 (`from(entity)` 패턴 사용)
- Controller에서 변환(toCommand, from)을 직접 호출하는 것을 지향한다.

### application 레이어
- Service: 비즈니스 오케스트레이션 중심으로 유지
- Command: presentation → application 간 데이터 전달 객체 (record 사용 권장)
- Exception: `BusinessException` 하위 구현체로 정의

### domain 레이어
- Entity: 핵심 비즈니스 규칙 포함, 상태 변경은 도메인 메서드로만 처리
- Repository: 인터페이스 정의 (구현체는 infrastructure에 위치)

### infrastructure 레이어
- JPA Repository 구현체, 외부 시스템 연동, 설정 클래스

## Command 사용 원칙
- presentation → application 간 데이터 전달에 사용
- Java record로 작성한다.
- 예) `MemberCreateCommand`, `RunCreateCommand`

## Request/Response DTO 사용 원칙
- Request: Controller에서 `toCommand()`를 호출하여 Command로 변환한다.
- Response: `from(entity)` 정적 팩토리 메서드 패턴으로 생성한다.
- `@JsonIgnoreProperties(ignoreUnknown = true)` 설정을 필요 시 추가한다.

## 엔티티 작성 규칙
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 필수 (JPA 프록시 생성 허용, 외부 직접 생성 방지)
- 생성은 `static` 팩토리 메서드(`create(...)`)로만 한다. 생성자 직접 호출 금지.
- 상태 변경은 반드시 도메인 메서드(예: `changeNickname`)로만 수행한다.
- `@AllArgsConstructor` 는 필요한 경우에만 사용하며, `public` 생성자 노출 최소화한다.
- Not Column 사용은 최소화한다. (엔티티 변수 총 개수의 1/4 이하)

## 예외 처리 규칙
- 비즈니스 예외는 `BusinessException` 하위 구현체로 추상화 가능한 레벨로 만든다.
    - (o) 공통적으로 활용 가능: `EntityNotFoundException`, `DuplicateException`
    - (x) 도메인 특화 남발: `MemberDuplicateEmailException`
- 에러 코드는 `ErrorCode` enum에 도메인별로 그룹화하여 관리한다.
- 에러 메시지는 `ErrorCode`에 한글로 직접 정의한다.

## API 응답 규칙
- 모든 응답은 `ApiResponse<T>` 래퍼로 반환한다.
- 성공: `ApiResponse.success(data)`
- 실패: `GlobalExceptionHandler`에서 `ApiResponse.error(errorCode, message)` 반환
- HTTP 상태 코드는 `ErrorCode`에 정의된 `HttpStatus`를 따른다.

## 유효성 검증 규칙
- 입력값 형식 검증(빈값, 길이, 형식): `@Valid` + Bean Validation 어노테이션 사용
- 비즈니스 규칙 검증(중복, 존재여부 등): Service 단에서 처리
- 검증 실패 시 `BusinessException` 하위 예외를 throw한다.

## Service / Method 책임 분리
- 하나의 Service에 조회/파싱/변환/실행 책임을 몰아넣지 않는다.
- Service 메서드는 오케스트레이션 중심으로 유지하고, 파싱/변환 로직은 별도 클래스로 분리한다.
- 분리 기준
    - 외부 연동/조회: `*Service`
    - 문자열/JSON/XML 파싱: `*ParserService`
    - 도메인/DTO 매핑: `*Converter`, `*Mapper`
- 하나의 메서드가 여러 단계(조회 + 파싱 + 매핑 + 후처리)를 모두 처리하면 메서드 분리한다.
- 코드리뷰 시, 단일 책임 원칙(SRP) 위반 여부를 필수 확인한다.

## 쿼리 작성 규칙
- 비즈니스 로직을 쿼리문에서 처리하지 않는다.
    - 복잡한 조회 로직은 서비스 단에서 처리한다.
    - JOIN이 많아질 경우 조회 성능이 저하되므로 최소화한다.
- N+1 문제를 항상 확인하고, 필요 시 Fetch Join 또는 Batch Size 설정을 사용한다.

## 메서드 시그니처 줄바꿈 규칙
- 파라미터 2~3개는 한 줄로 작성한다.
    - 예) `private void validateDuplicate(String email, String nickname) {`
- 파라미터가 많아 가독성이 떨어질 때(권장: 4개 이상)만 줄바꿈한다.
- 불필요한 줄바꿈(파라미터가 적은데 줄을 나누는 형태)은 사용하지 않는다.

## 복잡한 로직의 경우 주석 활용
- 로직이 복잡한 경우, 주석으로 설명을 적는다. 마지막에 `// by. 작성자명` 을 붙여준다.
- 본인이 작성한 주석이 아니더라도, 로직을 수정하는 경우 주석 수정 필수
- 복잡하지 않은 로직은 주석을 삭제한다. (메서드명이 곧 주석 역할을 해야 함)

## Gson 사용 금지
- 의존성 충돌 방지를 위해 Gson 사용 금지
- 대신 `ObjectMapper` 사용

## TDD 준수
- AC(인수 조건) 작성 후 테스트 코드 먼저 작성한다.
- 개발 순서: AC 작성 → 테스트 케이스 작성 → 구현 코드 작성 → 리뷰 → PR

---

## 프론트엔드 컨벤션 (index.html / React SPA)

### 기술 스택
- React 18 (CDN, functional components + hooks만 사용)
- Tailwind CSS (CDN, 커스텀 색상 포함)
- Kakao Maps JavaScript API
- Babel Standalone (JSX 트랜스파일)

### 컴포넌트 구조
```
App
├── AuthScreen          # 로그인/회원가입
├── Toast               # 전역 토스트 알림
├── BottomTabs          # 하단 탭 네비게이션
└── Tab 컴포넌트
    ├── MapTab          # 현재 위치 지도
    ├── DrawCourseTab   # 코스 그리기 (핵심 기능)
    ├── RankingTab      # 코스 랭킹/탐색
    └── ProfileTab      # 프로필/메달
        └── CourseCard  # 코스 카드 (지도 미리보기 포함)

공유 컴포넌트
├── MapComponent        # 카카오맵 래퍼 (fallback 포함)
└── Spinner             # 로딩 인디케이터
```

### 상태 관리 원칙
- `useState` / `useCallback` / `useRef` / `useEffect` hooks만 사용한다.
- 전역 공유 상태(`member`, `toast`, `activeTab`)는 `App`에서 관리하고 props로 전달한다.
- 탭 컴포넌트 내부 상태(로딩, 폼, 지도 객체 등)는 각 컴포넌트 로컬 상태로 관리한다.
- 상태 업데이트 시 이전 상태를 기반으로 하는 경우 함수형 업데이트를 사용한다.
  ```js
  setDraft(prev => ({ ...prev, waypoints: [...prev.waypoints, coord] }));
  ```

### API 통신 규칙
- 모든 서버 통신은 `apiFetch` 함수를 통해 처리한다.
- API 엔드포인트는 `api` 객체에 도메인별로 그룹화하여 정의한다.
- 응답 포맷: `{ success: boolean, data: T, error?: { message: string } }`
- 에러 시 `error.message`를 `showToast`로 사용자에게 표시한다.
- 프론트 좌표 포맷(`{ lat, lng }`)과 백엔드 포맷(`{ latitude, longitude, sequence }`)은 API 호출 시점에 변환한다.

### 지도(Kakao Maps) 사용 규칙
- 지도 로드는 `loadKakaoMaps()` Promise 함수를 통해 처리한다. 직접 스크립트 태그 추가 금지.
- `MapComponent`는 모든 지도 렌더링에 공유하며, `mapId` prop으로 인스턴스를 구분한다.
- 지도 객체(`mapObj`)와 Maps API(`mapsApi`)는 `onMapReady` 콜백으로 부모에 전달한다.
- 마커/폴리라인은 `useRef`로 참조를 보관하고, 재렌더 시 이전 객체를 `setMap(null)`로 제거 후 새로 그린다.
- 카카오맵 로드 실패 시 fallback 컨테이너를 렌더링하고, 클릭으로 좌표 입력이 가능하도록 한다.
- `useEffect` cleanup에서 `cancelled` 플래그로 언마운트 후 비동기 콜백 실행을 방지한다.

### 거리 계산
- 두 좌표 간 거리는 Haversine 공식을 사용한다. (`haversine` 함수)
- 웨이포인트 추가 시마다 `calcTotalDistance`로 자동 재계산한다.
- 표시 단위: km, 소수점 1자리.

### UI 디자인 규칙
- **다크 모드 + 네온 그린 포인트** 테마를 유지한다.
  - 배경 계층: `dark-900(#0a0a0a)` > `dark-800(#111111)` > `dark-700(#1a1a1a)` > `dark-600(#222222)` > `dark-500(#2a2a2a)`
  - 포인트 컬러: `neon(#39FF14)`
- **모바일 퍼스트**: 최대 너비 430px 고정, 전체 높이 `100dvh`.
- 탭 전환 시 `fade-enter` 클래스로 페이드 인 애니메이션을 적용한다.
- 버튼 비활성화: `disabled:opacity-50`, 터치 피드백: `active:opacity-70` 또는 `active:opacity-80`.

### 토스트 알림 규칙
- 전역 `showToast(message)` 함수를 props로 전달하여 사용한다.
- 같은 메시지를 연속 표시해야 할 경우 `setToast(null) → setTimeout` 패턴으로 강제 리렌더한다.
- 토스트는 2500ms 후 자동 소멸한다.
- 사용자 액션 결과(저장 성공, 좋아요 등)와 에러 메시지 모두 토스트로 표시한다.

### 빈 상태(Empty State) 처리 규칙
- 리스트가 비어 있을 때 반드시 안내 메시지와 다음 행동을 유도하는 문구를 표시한다.
- 로딩 중에는 `<Spinner />`를 표시하고, 완료 후 빈 상태 또는 데이터를 렌더링한다.
  ```js
  {loading ? <Spinner /> : list.length === 0 ? <EmptyState /> : <List />}
  ```
- 지도 로드 실패 시 fallback UI를 반드시 제공한다.

### 인증 흐름 규칙
- `member` 상태가 `null`이면 `<AuthScreen />`을 렌더링한다.
- 로그인/회원가입 성공 시 `onAuth(member)`를 호출하여 App 상태를 업데이트한다.
- 회원가입 완료 후 자동 로그인: `register()` → `login()` 순서로 호출한다.
- 인증은 서버 쿠키(JWT) 기반이며, 프론트에서 토큰을 직접 관리하지 않는다.

### 메달 계산 규칙
- 메달은 `calcMedals(member, myCourses)` 순수 함수로 계산한다.
- 렌더 시점에 동적으로 계산하며, 별도 상태로 저장하지 않는다.
- 메달 조건
  | 메달 | 조건 |
  |---|---|
  | Bronze Runner | 총 거리 ≥ 10km |
  | Silver Runner | 총 거리 ≥ 50km |
  | Gold Runner | 총 거리 ≥ 100km |
  | Course Creator | 생성 코스 수 ≥ 3개 |
  | Course Explorer | 코스 생성 지역 수 ≥ 5개 |
  | Running Legend | 내 코스 총 좋아요 ≥ 50개 |
