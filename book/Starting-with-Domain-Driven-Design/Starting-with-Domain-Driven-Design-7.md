# 도메인 주도 개발 시작하기

이 글은 [도메인 주도 개발 시작하기: DDD 핵심 개념 정리부터 구현까지](https://product.kyobobook.co.kr/detail/S000001810495) 책을 읽고 정리한 내용이다.

아래 내용은 책에서 중요한/기억하고자하는 내용 위주로 정리했다. 자세한 내용은 책을 참고하자.


---

## 7장. 도메인 서비스

### 7.1 여러 애그리거트가 필요한 기능

도메인 영역의 코드를 작성하다 보면 여러 애그리거트가 필요한 기능이 있다.

대표적인 예시가 `결제 금액 계산 로직` 이다.

* 상품 애그리거트: 구매하는 상품의 가격이 필요하다. 또한 상품에 따라 배송비가 추가되기도 한다.

* 주문 애그리거트: 상품별로 구매 개수가 필요하다.

* 할인 쿠폰 애그리거트: 쿠폰별로 지정한 할인 금액이나 비율에 따라 주문 총 금액을 할인한다. 할인 쿠폰을 조건에 따라 중복 사용할 수 있다거나 지정한 카테고리의 상품에만 적용할 수 있다는 제약 조건이 있다면 할인 계산이 복잡해진다.

* 회원 애그리거트: 회원 등급에 따라 추가 할인이 가능하다.

이 상황에서 실제 결제 금액을 계산해야 하는 주체는 어떤 애그리거트일까?

* `주문 애그리거트`를 주체로 갖돼, 하나의 애그리거트에 넣기 보다는 `도메인 서비스`를 이용해서 도메인 개념을 명시적으로 드러내면 된다.


### 7.2 도메인 서비스

도메인 서비스는 **상태 없이 로직만 구현**하기 때문에, 로직을 실행하는 데 필요한 값들은 메서드 파라미터를 통해 전달받는다.

* 예를 들어 `DiscountCalculationService` 는 할인 금액을 계산하는 데 필요한 주문 목록, 쿠폰, 회원 등급 정보를 모두 파라미터로 받아 처리한다.

```java
// 도메인 서비스 예시
public class DiscountCalculationService {
    public Money calculateDiscountAmounts(
            List<OrderLine> orderLines,
            List<Coupon> coupons,
            MemberGrade grade) {
        // ... 파라미터로 받은 값들을 이용해 복잡한 할인 로직 수행 ...
        return totalDiscountAmount;
    }
}
```

도메인 서비스를 사용하는 주체는 크게 **애그리거트와 응용 서비스**로 나뉜다.

#### 1. 애그리거트로 사용되는 경우

애그리거트 내부의 특정 기능(메서드)을 실행할 때 도메인 서비스를 파라미터로 전달하여, 그 기능의 일부를 위임할 수 있다.

* 예를 들어 Order 애그리거트의 `calculateAmounts()` 메서드가 도메인 서비스인 `DiscountCalculationService` 를 받아 최종 결제 금액을 계산하는 방식이다.

```java
// 애그리거트 예시
public class Order {
    // ... 필드 생략 ...

    public void calculateAmounts(DiscountCalculationService disCalSvc, MemberGrade grade) {
        Money totalAmounts = getTotalAmounts();
        // 할인 계산 로직을 직접 수행하지 않고, 도메인 서비스에 위임한다.
        Money discountAmounts = disCalSvc.calculateDiscountAmounts(this.orderLines, this.coupons, grade);
        this.paymentAmounts = totalAmounts.minus(discountAmounts);
    }
}
```

#### 2. 응용 서비스가 사용되는 경우

애그리거트에게 **도메인 서비스를 연결해주는 책임**은 응용 서비스에 있다.

응용 서비스는 주문 생성과 같은 전체 흐름을 제어하면서, 필요한 시점에 애그리거트 객체에 도메인 서비스를 주입하여 호출한다.

```java
// 응용 서비스 예시
public class OrderService {
    private DiscountCalculationService discountCalculationService; // 응용 서비스는 도메인 서비스를 알고 있다.

    // 주문 생성 기능
    private Order createOrder(OrderNo orderNo, OrderRequest orderReq) {
        Member member = findMember(orderReq.getOrdererId());
        Order order = new Order(/* ... 주문 정보 ... */);

        // 응용 서비스가 애그리거트 메서드를 호출할 때,
        // 자신이 알고 있는 도메인 서비스를 파라미터로 넘겨준다.
        order.calculateAmounts(this.discountCalculationService, member.getGrade());
        return order;
    }
}
```

이처럼 도메인 서비스를 활용하면, **복잡한 계산 로직을 애그리거트로부터 분리**하여 애그리거트는 자신의 핵심 책임(상태 관리)에 집중하게 하고, 

코드의 응집도와 가독성을 높일 수 있다.

> 정리

애그리거트의 상태를 변경하거나 상태 값을 계산하는 것은 모두 `도메인 로직`에 해당하며,

도메인 로직이 하나의 애그리거트에 넣기 복잡할 때, 그 로직을 `도메인 서비스`로 구현하면 된다.

---

외부 시스템이나 타 도메인간의 연동 기능도 도메인 서비스가 될 수 있다.

도메인 서비스는 도메인 로직을 표현하므로, 도메인 서비스의 위치는 다른 도메인 구성요소와 동일한 패키지에 위치한다.

* 예를 들어 주문 금액 계산을 위한 도메이 서비스는 아래 그림 7.1 과 같이 주문 애그리거트와 같은 패키지에 위치한다.

* 도메인 서비스의 개수가 많거나 엔티티나 밸류와 같은 다른 구성요소와 명시적으로 구분하고 싶다면, domain 패키지 밑에 domain.model, domain.service, domain.repository와 같이 하위 패키지를 구분하여 위치시켜도 된다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-7-1.png)


또한, 도메인 서비스의 로직이 고정되어있지 않은 경우 도메인 서비스 자체를 **인터페이스로 구현**하고 이를 구현한 클래스를 둘 수 있다.

특히 도메인 로직을 외부 시스템이나 별도 엔진을 이용해서 구현할 때 인터페이스와 클래스를 분리하게 된다.

* 예를 들어 할인 금액 계산 로직을 Rule 엔진을 이용해서 구현한다면 아래 그림 7.2 와 같이 구분할 수 있다.

* 아래처럼 도메인 서비스의 구현이 특정 구현 기술에 의존하거나 외부 시스템의 API를 실행한다면 **도메인 영역의 도메인 서비스는 인터페이스로 추상화해야한다.**

* 이를 통해 특정 구현에 종속되는 것을 방지할 수 있고 도메인 영역에 대한 테스트가 쉬워진다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-7-2.png)


---

## Review

* 도메인 주도 설계의 주요 개념 중 하나인 '도메인 서비스' 에 대해 기존 '도메인' 과 어떤 차이점을 가지는지 알게 되었다.

* 8장은 트랜잭션에 대한 부분인데, 이 부분은 도메인 주도 설계의 방법론적인 내용 보다는 동시성에 필요한 락 관리에 대한 설명을 다루는 부분이 더 큰 것 같다. 그래서 따로 정리는 해두지 않았다.
