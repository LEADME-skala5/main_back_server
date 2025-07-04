# Main Backend Server

## 프로젝트 개요

이 프로젝트는 Spring Boot 기반의 백엔드 서버로, 사용자 인증 및 관리, 보고서, 평가, 활동 로그 등의 기능을 제공합니다. JWT 토큰 기반의 인증 시스템을 갖추고 있으며, 다양한 데이터베이스와 연동되어
있습니다.

### 주요 기능

- **사용자 인증 및 관리**: JWT 토큰 기반의 로그인, 로그아웃, 토큰 갱신 기능
- **보고서 관리**: 사용자 보고서 생성, 조회, 수정, 삭제 기능
- **평가 시스템**: 사용자 평가 기능
- **활동 로그**: 사용자 활동 추적 및 로깅 기능

## 기술 스택

### 백엔드

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.5.0
- **보안**: Spring Security
- **데이터베이스**:
    - MariaDB (주 데이터베이스)
    - MongoDB (문서 저장용)
    - Redis (캐싱 및 토큰 관리)
- **ORM**: Spring Data JPA
- **API 문서화**: SpringDoc OpenAPI (Swagger)
- **인증**: JWT (io.jsonwebtoken)
- **기타**: Lombok (코드 간소화)

### 빌드 도구

- Gradle

## 로컬 개발 환경 설정

### 필수 요구사항

- JDK 21
- Gradle
- MariaDB
- MongoDB
- Redis

### 로컬 실행 방법

1. **저장소 클론**
   ```bash
   git clone [저장소 URL]
   cd main_back_server
   ```

2. **데이터베이스 설정**
    - MariaDB 설치 및 데이터베이스 생성
    - MongoDB 설치
    - Redis 서버 실행

3. **환경 변수 설정**
    - `src/main/resources/properties/env.properties` 파일에 다음 설정 추가:
   ```properties
   # 데이터베이스 설정
   spring.datasource.url=jdbc:mariadb://localhost:3306/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   # MongoDB 설정
   spring.data.mongodb.uri=mongodb://localhost:27017/your_mongo_db

   # Redis 설정
   spring.data.redis.host=localhost
   spring.data.redis.port=6379

   # JWT 설정
   jwt.secret=your_jwt_secret_key
   jwt.access-token-validity=3600
   jwt.refresh-token-validity=86400
   ```

4. **어플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```
   또는
   ```bash
   ./gradlew build
   java -jar build/libs/main_server-0.0.1-SNAPSHOT.jar
   ```

5. **서버 접속**
    - 기본 포트: 8080
    - API 접근: http://localhost:8080/api/v1
    - Swagger UI: http://localhost:8080/swagger-ui.html

## 프로젝트 구조

```
src/main/java/com/example/main_server/
├── MainServerApplication.java       # 애플리케이션 진입점
├── auth                            # 인증 관련 코드
│   ├── jwt                         # JWT 토큰 처리
│   └── user                        # 사용자 관리
├── config                          # 애플리케이션 설정
├── common                          # 공통 클래스 및 유틸리티
├── exception                       # 예외 처리
├── report                          # 보고서 관련 기능
├── evaluation                      # 평가 시스템
│   ├── peer                        # 동료 평가
│   ├── quantitative                  # 실적(정량) 평가
│   └── qualitive                   # 태도(정성) 평가
├── activityLog                     # 활동 로깅
└── util                            # 유틸리티 클래스
```

## 배포

본 프로젝트는 GitHub Actions를 활용하여 CI/CD 파이프라인을 구성하였습니다. 배포 과정은 아래와 같은 순서로 이루어집니다:

1. GitHub Actions를 통한 Docker 이미지 빌드
2. main 브랜치로 코드가 푸시되면 GitHub Actions가 자동으로 실행됩니다.
3. Dockerfile을 기반으로 백엔드 애플리케이션의 이미지를 빌드합니다.
4. Docker Hub에 이미지 푸시
5. 빌드된 이미지는 프로젝트의 Docker Hub 리포지토리에 latest 혹은 sha 태그와 함께 푸시됩니다.
6. 이미지 해시코드 확인
7. 빌드 완료 후 GitHub Actions 로그에서 생성된 이미지의 **해시값(SHA256 Digest)**를 확인합니다.
8. infra 레포지토리 YAML 수정
9. infra 레포지토리의 deployment.yaml 또는 values.yaml 파일에서 image 값을 위에서 확인한 해시값으로 변경합니다.

## 💻 Commit Convention

| 태그 이름      | 설명                                       |
|------------|------------------------------------------|
| [chore]    | 코드 수정, 내부 파일 수정                          |
| [feat]     | 새로운 기능 구현                                |
| [add]      | FEAT 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 |
| [hotfix]   | issue나 QA에서 급한 버그 수정에 사용                 |
| [fix]      | 버그, 오류 해결                                |
| [del]      | 쓸모 없는 코드 삭제                              |
| [docs]     | README나 WIKI 등의 문서 개정                    |
| [correct]  | 주로 문법의 오류나 타입의 변경, 이름 변경에 사용             |
| [move]     | 프로젝트 내 파일이나 코드의 이동                       |
| [rename]   | 파일 이름 변경이 있을 때 사용                        |
| [improve]  | 향상이 있을 때 사용                              |
| [refactor] | 전면 수정이 있을 때 사용                           |
| [style]    | CSS 및 스타일 수정시 사용                         |
| [link]     | 라우팅 연결 시 사용                              |

예시: `feat: 로그인 기능 추가`
