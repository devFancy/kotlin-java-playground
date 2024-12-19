# Spring Boot Kotlin Build Manager

> `Spring Boot Kotlin Build Manager`는 Kotlin, Spring Boot 기반 프로젝트의 빌드 및 의존성 관리를 고객사별로 분리하여 관리할 수 있도록 설계된 빌드 환경입니다. 

이 프로젝트는 다음과 같은 문제를 해결하기 위해 설계되었습니다.
* **공통 설정**과 **고객사별 설정**의 명확한 분리
* 고객사별 의존성 및 버전을 동적으로 적용
* 유지보수성을 개선하고, 설정 충돌을 방지


---

## 디렉토리 구조

```plaintext
springboot-kotlin-build-manager/
├── build.gradle.kts       # 빌드 스크립트 (Kotlin DSL)
├── settings.gradle.kts    # 플러그인 및 프로젝트 설정
├── gradle/
│   ├── dependencies/      # 고객사별 의존성 정의
│   │   ├── default.gradle
│   │   └── customer.gradle
├── gradle.properties      # 공통 및 고객사별 속성 관리
└── src/                   # 프로젝트 소스
```

---

## 개발 환경

* Gradle: 8.1.1 

* Kotlin: 1.9.25 

* Java: 21 

* Spring Boot: 3.2.x (기본값) / 3.1.x (고객사 설정)


---

## 사용 방법

### 기본값으로 실행

* 기본(Spring Boot 3.2.x) 설정으로 빌드하려면 다음과 같은 명령어를 사용한다.

```bash
./gradlew clean bootJar
```

* 추가적으로 빌드 시 의존성을 확인하려면 다음과 같은 명령어를 사용한다.

```bash
./gradlew clean bootJar dependencies --configuration runtimeClasspath
```

### 고객사별 설정으로 실행

* 고객사(Spring Boot 3.1.x) 설정으로 빌드하려면 `-Pcustomer=<고객사명>` 옵션을 추가한다.

* 아래 고객사 이름은 `gradle.properties`의 customer 키와 일치해야 한다.

  * 고객사가 `goldBank`인 경우

```bash
./gradlew clean bootJar -Pcustomer=goldbank
```

* 의존성을 확인하며 빌드하려면 다음과 같은 명령어를 사용한다.

```bash
./gradlew clean bootJar -Pcustomer=goldBank dependencies --configuration runtimeClasspath
```


---

## 참고

### 의존성 확인 방법

* `dependencies --configuration runtimeClasspath` 옵션은 프로젝트의 런타임 의존성을 확인할 때 유용하다.

  * 이 옵션을 추가하면 현재 설정된 버전의 의존성 트리를 출력하여, 의존성 충돌이나 중복 문제를 쉽게 식별할 수 있다.

  * 예제

```bash
./gradlew dependencies --configuration runtimeClasspath
```
