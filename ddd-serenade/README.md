# 📬 [DDD 세레나데 7기] 미션 정리

> 넥스트스텝의 [DDD 세레나데 7기](https://edu.nextstep.camp/c/GwN2MSqv) 강의를 듣고, 정리한 내용입니다.
>
> 기간: 2025.01.21 ~ 2025.03.02
> 
> 주차별 미션이 완료된 부분에 `(PR) 링크` 를 추가했습니다.

## 1주차: 도메인 주도 설계 이해

* 0단계: [JUnit 5 학습](https://github.com/next-step/ddd-legacy/pull/725)

* 1단계: [문자열 덧셈 계산기](https://github.com/next-step/ddd-legacy/pull/764)

* 2단계: [요구 사항 정리](https://github.com/next-step/ddd-legacy/pull/790)

* 3단계: [테스트를 통한 코드 보호](https://github.com/next-step/ddd-legacy/pull/835)

## 2주차: 크게 소리 내어 모델링 하기

* 1단계: [용어 사전 만들기](https://github.com/next-step/ddd-strategic-design/pull/494)

* 2단계: [모델링 하기](https://github.com/next-step/ddd-strategic-design/pull/507)

* 3단계: [기능 우선 패키지 구성하기](https://github.com/next-step/ddd-strategic-design/pull/509)

## 3주차: 도메인 주도 설계 기본 요소

* 0단계 - 기본 코드 준비
    * 첫 번째 [PR](https://github.com/next-step/ddd-tactical-design/pull/347)
    * 두 번째 [PR](https://github.com/next-step/ddd-tactical-design/pull/352)

* 1단계 - [리팩터링(상품)](https://github.com/next-step/ddd-tactical-design/pull/357)

* 2단계 - 리팩터링(메뉴)
    * 첫 번째 [PR](https://github.com/next-step/ddd-tactical-design/pull/366)
    * 두 번째 [PR](https://github.com/next-step/ddd-tactical-design/pull/373)

* 3단계 - [리팩터링(매장 식사 주문)](https://github.com/next-step/ddd-tactical-design/pull/376)

## 4주차: 도메인 주도 설계 아키텍처

> 4주차는 미션이 없습니다.

* 계층형 아키텍처

* DTO(Data Transfer Object)

* 전략적 설계 - ANTICORRUPTION LAYER

## 5주차: 도메인 이벤트와 CQRS

> 5주차는 미션이 없습니다.

* 전술적 설계 - DOMAIN EVENT

* 이벤트 소싱(Event Sourcing)

* CQRS(명령 및 쿼리 책임 분리)

---

## 복습(+메모)

> 아래의 글은 해당 강의를 들으면서 배웠던 개념들과 개인 생각을 덧붙여서 정리한 내용입니다.

### 1주차 복습

> 도메인, 모델, 도메인 모델, 도메인 주도 설계에 대해 한 줄로 정리하기

* `도메인` - **소프트웨어로 해결하고자 하는 문제 영역** 입니다.
  * 예를 들어, 핸드폰을 이용하여 사용자 간에 거래를 할 때 필요한 도메인은 `송금`입니다. 
  * 송금 도메인을 제대로 이해하려면, 송금과 관련된 금융, 계좌, 수수료 등의 지식이 필요합니다.

* `모델` - 목적을 위해 현실 세계에 존재하는 것을 **가공하고 편집**하여 우리에게 정보를 제공하는 것입니다.
  * 예를 들어, 금융 송금 시스템에서는 실제 은행 계좌, 송금 금액, 수수료, 거래 일자 등의 정보를 단순화하여 송금 요청, 송금 내역 등의 모델로 표현할 수 있습니다.

* `도메인 모델` - 도메인의 핵심 개념과 비즈니스 규칙, 행동(메서드)까지 모두 반영한 **목적을 가진 의사소통 수단**입니다. 이는 회의, 기획, 디자인, 개발 등 여러 단계에서 공유되어, 시스템의 핵심 개념을 풍부하게 표현하고 비즈니스 로직을 구현하는 데 사용됩니다.
  * 예를 들어, 금융 송금 도메인에서는 '송금', '계좌', '거래', '수수료' 등의 개념을 객체와 연관관계로 표현하여, 송금 내역 생성, 수수료 계산, 계좌 잔액 검증 등의 비즈니스 로직을 포함하는 도메인 모델을 구축할 수 있습니다.

* `도메인 주도 설계` - 비즈니스 관점에서 도메인을 중심으로 설계해 나가는 접근 방식입니다. 도메인 모델을 활용하여 복잡한 비즈니스 규칙과 요구사항을 코드에 명확하게 반영하고, 개발팀과 비즈니스 전문가 간의 효과적인 의사소통을 도모합니다.


---

### 이벤트 스토밍 워크숍 복습

* `이벤트 스토밍 워크숍` 이란? -> 프로젝트에 같이 참가한 팀원들이 서로가 공통된 이해를 가지기 위해 도메인 지식을 탐구하는 과정입니다.

* 내가 `퍼실리테이터`라면 어떤 마음가짐을 가져야 할까요? 
  *  참가자들이 자유롭게 아이디어를 내고 논의할 수 있도록 돕고, 개입은 최소화하되 필요할 때 적절한 방향을 제시하는 조력자 역할의 마음가짐을 가진다.

* 내가 `드라이버`라면 무엇을 염두에 두어야 할까요?
  * 도메인 이벤트를 논리적인 흐름에 맞게 배치하고, 팀원들과의 피드백을 주고받으며 조정합니다.

* 내가 `내비게이터라면` 드라이버를 돕기 위해 어떻게 안내해야 할까요?
  * 도메인 이벤트의 맥락과 연관성을 고려하여 드라이버가 올바른 결정을 내릴 수 있도록 논리적으로 설명하고 안내해 줍니다.


---

### 2주차 복습

* `도메인`이란 우리가 해결해야할 영역 → 보통은 비즈니스 도메인이라 부른다. (예시. 커머스)
* `하위 도메인`이란 도메인을 세부 도메인으로 나누는 것을 의미한다.
   * 예시. 전시, 카탈로그, 상품, 주문, 결제, 정산
   * 이런 하위 도메인은 **자연스럽게 도출**되는 개념이다. → 그만큼 하위 도메인은 제어할 수 없는 대상이다.
   * 하위 도메인 안에서 **비즈니스 가치가 크다면**, 하위 안의 하위 도메인을 만들 수 있다.

* `하위 도메인`과 `바운디드 컨텍스트`의 차이점은 무엇인가요?
  * 하위 도메인은 자연스럽게 나뉘며, 바운디드 컨텍스트는 인위적으로 나뉩니다.
  * 하위 도메인은 관심사이며, 바운디드 컨텍스트는 관심사를 구분짓는 경계로 간주됩니다.

* 언제 `바운디드 컨텍스트`를 분할해야 하나요?
  * 서비스가 커짐에 따라 복잡성이 높아질 때입니다.
  * 또는 모두가 동일하게 이해하고 개발하기 편하도록 경계가 필요할 때입니다.

* 요구 사항과 `도메인 모델링`의 차이점은 무엇인가요?
  * 요구사항과 모델링은 만드는 사람(=주체)에 따라 달라진다고 생각됩니다. 
    * 요구사항은 클라이언트가 우리에게 문제를 해결해달라는 의미이고,
    * 도메인 모델링은 그러한 문제를 해결하기 위한 아이디에이션(=아이디어 도출)입니다.
  * 이러한 접근 방법을 표현하기 위하여 다이어그램, 글, 그림 등 다양한 도구를 활용합니다.

* `도메인 엔티티`와 영속성 엔티티의 차이점은 무엇인가요?
  * 도메인 엔티티는 식별자를 갖는 무언가입니다.
  * 영속성 엔티티는 테이블 관점에서 도출됩니다.
  * 이 두 개(Persistence, 도메인 주도 설계)는 높은 확률로 동일할 수도 있고, 다를 수도 있습니다.

* 어떤 엔티티가 애그리거트에서 루트 엔티티가 될 수 있나요?
  * **외부에 노출되어 있는지 여부**에 따라 즉, 외부에 노출되어도 되는 엔티티인 경우 루트 엔티티가 될 수 있습니다.

* 애그리거트와 리포지토리는 어떤 관련이 있나요?
  * 애그리거트는 관련 객체들을 하나로 묶은 군집을 의미합니다.
    * 이는 객체의 타입이 아니라, 객체들을 묶어놓은 패키지 또는 논리적 구조를 말합니다.
  * `레포지토리`는 **애그리거트 단위로 도메인 객체를 저장하고 조회하는 기능을 정의**합니다.
    * **애그리거트(루트) 단위로 존재**하며 테이블 단위로 존재하지 않습니다.
    * 정리하면, JPARepository, CrudRepository와 같은 인터페이스를 상속받았기 때문에 레포지토리를 사용하게 되는 것입니다.
    * 이는 도메인 주도 설계 관점에서의 레포지토리가 아닙니다.
