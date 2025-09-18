# 도메인 주도 개발 시작하기

이 글은 [도메인 주도 개발 시작하기: DDD 핵심 개념 정리부터 구현까지](https://product.kyobobook.co.kr/detail/S000001810495) 책을 읽고 정리한 내용이다.

아래 내용은 책에서 중요한/기억하고자하는 내용 위주로 정리했다. 자세한 내용은 책을 참고하자.


---

# 2장. 아키텍처 개요

- `키워드`: 아키텍처, DIP, 도메인 영역의 주요 구성요소, 인프라스트럭처, 모듈

## 2.1 네 개의 영역

- 표현, 응용, 도메인, 인프라스트럭처

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-1.png)

- 표현 영역: 사용자의 요청을 해석 → 응용 서비스에 전달, 응용 서비스의 실행 결과를 받아와서 → 사용자에게 전달
- 응용 영역: 시스템이 사용자에게 제공해야 할 기능을 구현 - 주문 등록, 주문 취소, 상품 상세 조회

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-2.png)

- 도메인 영역: 도메인 모델을 구현한다. Order, OrderLine 같은 것들
- 인프라스트럭처: 구현 기술에 대한 것을 다룬다. RDBMS, 메시지 큐

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-3.png)

## 2.3 DIP

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-4.png)

- `고수준 모듈`: 의미 있는 단일 기능을 제공하는 모듈로 여기서 CacluateDiscountService 는 ‘가격 할인 계산’ 이라는 기능을 구현한다.
    - 고수준 모듈의 기능을구현하려면 여러 하위 기능이 필요하다.
    - 가격 할인 계산 기능을 구현하려면 고객 정보를 구해야 하고 룰을 실행해야 하는데, 이 두 기능이 하위 기능이다.
- `저수준 모듈`: 하위 기능을 실제로 구현한 것이다.

여기서 고수준 모듈이 제대로 동작하려면 저수준 모듈을 사용해야 한다.

- 고수준 모듈의 의존 문제 → 저수준 모듈이 바뀜에 따라 고수준 모듈에 영향을 주고 그에 따라 코드를 고쳐야 한다.
    - 고수준 모듈이 저수준 모듈을 의존
    - 저수준 모듈의 변경에 따라 고수준 모듈이 영향을 받는다.
    - 고수준 모듈만 테스트하기 어렵다.
    - 다른 구현 기술을 사용하려면 코드의 많은 부분을 고쳐야 한다.
- 이를 해결하기 위해 **저수준 모듈이 고수준 모듈에 의존하게 하는 방법**인 `DIP`를 적용한다.
    - 즉, Infra 레이어가 Domain 레이어, Application 레이어에 의존하게 만드는 것 → 추상화한 인터페이스를 고수준 모듈에서 만든다.
    - 고수준 모듈은 더 이상 저수준 모듈에 의존하지 않고 구현을 **`추상화한 인터페이스`**에 의존한다.
    - 누가 되었든 간에, 추상화된 인터페이스를 고수준 모듈 관점에서 만들고, 그 인터페이스에 대한 실제 구현을 구글 지도, 네이버 지도 같은 api 가 만드는 것 → DIP
        - 역할을 추출한 것 → DIP

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-5.png)

- CalculateDiscountService 입장에서 봤을 때 구현 부분은 중요치 않고, ‘고객 정보와 구매 정보에 룰을 적용해서 할인 금액을 구한다’라는 것만 중요하다. 이를 추상화한 인터페이스는 다음과 같다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-6.png)

- CalculateDisco니ntService에는 Drools에 의존하는 코드가 없다. 실제 RuleDiscounter의 구현 객체는 생성자를 통해 전달받는다.
- 실제 구현체는 RuleDiscounter 인터페이스를 상속받아 구현한다.
  
![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-7.png)

- 위 그림 2.8를 통해 보면 RuleDiscounter 인터페이스는 고수준 모듈에 속한다. → 왜? ‘룰을 이용한 할인 금액 계산’ 은 고수준 모듈의 개념이기 때문이다.
- 구현체인 DroolsRuleDiscounter는 고수준의 하위 기능인 RuleDiscounter 를 구현한 것이므로 저수준 모듈에 속한다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-8.png)

- 그림 2.9처럼 저수준 모듈이 고수준 모듈을 의존하는 경우를 DIP(의존 역전 원칙)이라 부른다.


### 2.3.1 DIP 주의사항

- DIP의 핵심은 고수준 모듈이 저수준 모듈에 의존하지 않도록 하기 위함이다.
- DIP를 적용할 때 **하위 기능을 추상화한 인터페이스**는 `고수준 모듈` 관점에서 도출한다.

### 2.3.2 DIP와 아키텍처

인프라스트럭처 영역은 구현 기술을 다루는 저수준 모듈이고,

응용 영역과 도메인 영역은 고수준 모듈이다.

아키텍처에 DIP를 적용하면 아래와 같이 인프라스트럭처 영역이 응용 영역과 도메인 영역에 의존(상속)하는 구조가 된다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-9.png)

- 위 그림 2.13 처럼 주문 시 통지 방식에 SMS를 추가하는 요구사항이 들어왔을 때, 응용 영역의 OrderService 는 변경할 필요가 없다.
- 아래 그림 2.14 처럼 두 통지 방식을 함께 제공하는 Notifier 구현 클래스를 인프라스트럭처 영역에 추가하면 된다.
- MyBatis 대신 JPA를 구현 기술로 사용하고 싶다면 JPA를 이용한 OrderRepository 구현 클래스를 인프라스트럭처 영역에 추가하면 된다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-10.png)

- “잠깐” 부분처럼 DIP를 항상 적용할 필요는 없고, 구현 기술과 요구사항에 따라 적절히 도입하자.


## 2.4 도메인 영역의 주요 구성요소

도메인 영역의 모델은 도메인의 주요 개념을 표현하며 핵심 로직을 구현한다.

아래 표 2.1 과 같이 도메인 영역의 주요 구성 요소는 다음과 같다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-11.png)

### 2.4.1 엔티티와 밸류

- 엔티티와 밸류에 대한 개념은 1장 부분을 참고하자.

DB 테이블의 엔티티와 도메인 모델의 엔티티 는 다르다.

두 모델의 가장 큰 차이점은

- 도메인 모델의 엔티티는 **데이터와 함께 도메인 기능을 함께 제공한다는 점**이다.
    - 예를 들어, 주문을 표현하는 엔티티는 주문과 관련된 데이터 뿐만 아니라 배송지 주소 변경을 위한 기능을 함께 제공한다.
    - 도메인 모델의 엔티티는 데이터와 함께 기능을 제공하는 객체이다. 도메인 관점에서 기능을 구현하고 기능 구현을 캡슐화해서 데이터가 임의로 변경되는 것을 막는다.
- 또 다른 차이점은 도메인 모델의 엔티티는 **두 개 이상의 데이터가 개념적으로 하나인 경우 밸류 타입을 이용해서 표현할 수 있다.**
    - 위 코드에서 주문자를 표현하는 `Orderer` 는 밸류 타입으로 아래와 같이 주문자 이름과 이메일 데이터를 포함할 수 있다.

    ![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-12.png)

    - RDBMS 와 같은 관계형 DB는 밸류 탕비을 제대로 표현하기 힘들다.
    - Order 객체의 데이터를 저장하기 위해서는 아래 그림 2.15처럼 왼쪽 혹은 오른쪽과 같은 방법으로 저장해야한다.

    ![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-13.png)

- 1장에서 설명한 것처럼 밸류는 `불변`으로 구현할 것을 권장하며, 이는 엔티티의 밸류 타입 데이터를 변경할 때는 `객체 자체를 완전히 교체한다는 것`을 의미한다.
    - 예를 들어 배송지 정보를 변경하는 코드는 기존 객체의 값을 변경하지 않고 다음과 같이 새로운 객체를 필드에 할당한다.

    ![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-14.png)

### 2.4.2 애그리거트

- 도메인이 커질수록 개발할 도메인 모델도 커지면서 많은 엔티티와 밸류가 출현한다.
- 엔티티와 밸류 개수가 많아질수록 모델은 점점 더 복잡해진다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-15.png)

- 도메인 모델도 개별 객체뿐만 아니라 상위 수준에서 모델을 볼 수 있어야 전체 모델의 관계와 개별 모델을 이애하는데 도움이 된다.
- 이처럼 도메인 모델에서 전체 구조를 이해하는 데 도움이 되는 것이 바로 `애그리거트`다. (동사로는 `애그리게이트`라고 부른다)
    - 애그리거트의 대표적인 예가 `주문`이다. 주문 이라는 도메인 개념은 ‘주문’, ‘배송지 정보’, ‘주문자’, ‘주문 목록’, ‘총 결제 금액’의 하위 모델로 구성되며 이 하위 개념을 표현한 모델을 하나로 묶어서 ‘주문’ 이라는 상위 개념으로 표현할 수 있다.
- 애그리거트를 사용하면 관련 객체를 묶어서 `객체 군집` 단위로 모델을 바라본다.
- 애그리거트는 군집에 속한 객체를 관리하는 `루트 엔티티`를 갖는다.
    - 루트 엔티티는 애그리거트에 속해 있는 엔티티와 밸류 객체를 이용해서 애그리거트가 구현해야할 기능을 제공한다.
    - 애그리거트 루트를 통해 간접적으로 애그리거트 내의 다른 엔티티나 밸류 객체에 접근한다.
    - 이것은 애그리거트의 내부 구현을 숨겨서 애그리거트 단위로 구현을 캡슐화할 수 있도록 돕는다.

    ![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-16.png)

    - 애그리거트 루트인 Order는 주문 도메인 로직에 맞게 애그리거트의 상태를 관리한다.
    - 예를 들어, Order의 배송지 정보 변경 기능은 배송지를 변경할 수 있는지 확인한 뒤에 배송지 정보를 변경한다.
    - 여기서 배송지 정보 변경 관련 메서드는 주문 애그리거트 Order 통해 구현되어야한다.

- 애그리거트 구현에 대한 자세한 내용은 3장을 살펴보자.

### 2.4.3 리포지터리

- `리포지터리`는 **구현을 위한 도메인 모델**이다.
    - 엔티티나 벨류는 요구사항에서 도출되는 도메인 모델이다.
- 리포지터리는 애그리거트 단위로 도메인 객체를 저장하고 조회하는 기능을 정의한다.
- 도메인 모델 관점에서 OrderRepository는 도메인 객체를 영속화하는데 필요한 기능을 추상화한 것으로 고수준 모듈에 속한다. 
- 기반 기술을 이용해서 OrderRepository를 구현한 클래스는 저수준 모듈로 인프라스트럭처 영역에 속한다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-17.png)

- 전체 모듈구조는 위 2.19 그림과 같다.
- 응용 서비스는 의존 주입과 같은 방식을 사용해서 실제 레포지터리 구현 객체에 접근한다.

리포지터리를 사용하는 주체가 응용 서비스이기 때문에 리포지터리는 응용서비스가 필요로 하는 메서드를 아래와 같이 기본적으로 제공한다.

- 애그리거트를 저장하는 메서드
- 애그리거트 루트 식별자로 애그리거트를 조회하는 메서드
- 이 외에 필요에 따라 delete, counts() 등의 메서드를 제공하기도 한다.
- 리포지토리를 구현하는 방법은 선택한 구현 기술에 따라 다르며, 자세한 내용은 4장을 살펴보자.


## 2.5 요청 처리 흐름

요청 처리 흐름은 아래와 같다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-18.png)


## 2.6 인프라스트럭처 개요

영속성 처리를 위해 JPA를 사용할 경우 Entity or Table 어노테이션을 도메인 모델 클래스에 사용하는 것이 XML 매핑 설정을 이용하는 것보다 편리하다.

DIP의 장점을 해치지 않는 범위에서 응용 영역과 도메인 영역에서 구현 기술에 대한 의존을 가져가는 것이 나쁘지 않다고 생각한다.

응용 영역과 도메인 영역이 인프라스트럭처에 대한 의존을 와전히 갖지 않도록 시도하는 것은 자칫 구현을 더 복잡하고 어렵게 만들 수 있다.


## 2.7 모듈 구성

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-19.png)

도메인 모듈은 도메인에 속한 애그리거트를 기준으로 다시 패키지를 구성한다.

예를 들어 카탈로그 하위 도메인이 상품과 카테고리로 구성될 경우 아래 그림 2.23과 같이 도메인을 두 개의 하위 패키지로 구성할 수 있다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-2-20.png)

도메인이 복잡하면 `도메인 모델`과 `도메인 서비스`를 다음과 같이 별도 패키지에 위치시킬 수도 있다.

- com.myshop.order.domain.order： 애그리거트 위치
- com.myshop.order.domain.service： 도메인 서비스 위치

모듈 구조를 얼마나 세분화해야 하는지에 대해 정해진 규칙은 없다.

개인적으로 한 패키지에 가능하면 10~15개 미만으로 타입 개수를 유지하려고 노력한다.

이 개수를 넘어가면 패키지를 분리하는 시도를 해본다.

