# 🌱 Hackerton - 8th COKERTHON Team 4

우울감을 극복하는 단계별 미션 트랙 서비스입니다. 사용자는 3단계 트랙을 순서대로 완료하며 일상 회복을 도전합니다.

---

## 기술 스택

| 분류 | 사용 기술 |
|------|----------|
| Language | Java 21 |
| Framework | Spring Boot 4.1.0 |
| Database | MySQL + Spring Data JPA |
| Auth | JWT (Access 1h / Refresh 14d) |
| Docs | Springdoc OpenAPI 3.0 (Swagger UI) |
| Build | Gradle |
| Deploy | AWS EC2 |

---

## 핵심 도메인

### 트랙 (Track)

사용자는 온보딩 선택 후 3단계 트랙을 순서대로 진행합니다.

| 단계 | 트랙명 | 필요 일수 |
|------|--------|----------|
| 1 | 나를 돌보기 (SELF_CARE) | 14일 |
| 2 | 바깥으로 나가기 (GO_OUTSIDE) | 7일 |
| 3 | 사람과 연결하기 (CONNECT_PEOPLE) | 5일 |

### 미션 (Mission)

| 타입 | 설명 |
|------|------|
| TRACK_DEFAULT | 트랙 공통 미션 (전체 공유) |
| USER_CUSTOM | 개인 미션 (트랙당 최대 2개, SELF_CARE 불가) |

**하루 완료 조건**
- `SELF_CARE`: 공통 미션 전부 완료
- 그 외: 순환 공통 미션 1개 + 개인 미션 1개 이상

**트랙 진행 조건**: 필요 일수만큼 하루 완료 기준 충족 시 다음 트랙으로 전환

### 응원 (Cheer)

같은 트랙 멤버에게 하루 1회 응원 메시지를 보낼 수 있습니다. 최근 10개 응원을 조회할 수 있습니다.

---

## API 명세

### Auth `/api/auth`

| Method | Path | 설명 |
|--------|------|------|
| POST | `/signup` | 회원가입 |
| POST | `/login` | 로그인 (Access + Refresh 토큰 발급) |
| POST | `/reissue` | Access 토큰 재발급 |
| POST | `/logout` | 로그아웃 |

### Track `/api/tracks`

| Method | Path | 설명 |
|--------|------|------|
| POST | `/onboarding` | 트랙 배정 |
| GET | `/me` | 내 트랙 조회 |

### Mission `/api/missions`

| Method | Path | 설명 |
|--------|------|------|
| GET | `/today` | 오늘의 미션 목록 조회 |
| POST | `/{missionId}/complete` | 미션 완료 처리 |
| POST | `/proceed` | 다음 트랙으로 진행 |
| GET | `/progress` | 미션 진행 현황 조회 |
| POST | `/custom` | 개인 미션 추가 |
| GET | `/custom` | 개인 미션 목록 조회 |

### Cheer `/api/cheers`

| Method | Path | 설명 |
|--------|------|------|
| POST | `/` | 응원 보내기 (하루 1회) |
| GET | `/` | 최근 응원 10개 조회 |

---

## 응답 형식

모든 API는 아래 형식으로 응답합니다.

```json
{
  "isSuccess": true,
  "code": "MISSION200_1",
  "message": "미션 완료 처리에 성공했습니다.",
  "result": { }
}
```

---

## 미션 완료 흐름

```
POST /missions/{id}/complete
    ↓
trackCompleted: false  →  계속 미션 수행
trackCompleted: true   →  POST /missions/proceed 호출 → 다음 트랙으로 이동
```

---

## 환경 변수

```
DB_URL=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
```

---

## 실행 방법

```bash
./gradlew bootRun
```

Swagger UI (로컬): `http://localhost:8080/swagger-ui/`

Swagger UI (서버): https://13.125.129.40.nip.io/swagger-ui/index.html#/
