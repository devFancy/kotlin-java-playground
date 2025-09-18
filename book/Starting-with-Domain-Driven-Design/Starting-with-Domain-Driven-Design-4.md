# 도메인 주도 개발 시작하기

이 글은 [도메인 주도 개발 시작하기: DDD 핵심 개념 정리부터 구현까지](https://product.kyobobook.co.kr/detail/S000001810495) 책을 읽고 정리한 내용이다.

아래 내용은 책에서 중요한/기억하고자하는 내용 위주로 정리했다. 자세한 내용은 책을 참고하자.

(이미 아는 개념/예시는 정리하지 않았다.)

---

# 4장. 리포지터리와 모델 구현

## 4.1 JPA를 이용한 리포지터리 구현

도메인 모델과 리포지토리를 구현할 때 선호하는 기술은 JPA 이다.

데이터 보관소로 RDBMS를 사용할 때 객체 기반의 도메인 모델과 관계형 데이터 모델 간의 매핑을 처리하는 기술로 ORM 만한 것이 없다.

### 4.1.1 모듈 위치

2장에서 언급한 것처럼 리포지토리 인터페이스는 애그리거트와 같이 도메인 영역에 속하고 리포지토리를 구현한 클래스는 인프라 영역에 속한다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-4-1.png)


## 4.3 엔티티와 밸류 매핑

애그리거트와 JPA 매핑을 위한 기본 규칙은 다음과 같다.

- 애그리거트 루트는 엔티티이므로 `@Entity` 로 매핑을 설정한다.

한 테이블에 엔티티와 밸류 데이터가 같이 있다면

- 밸류 `@Embeddable`로 매핑 설정한다.
- 밸류 타입 프로퍼티는 `@Embedded`로 매핑 설정한다.

주문 애그리거트로 예를 들면, 주문 애그리거트의 루트 엔티티는 Order 이고 이 애그리거트에 속한 Orderer와 ShippingInfo 는 밸류이다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-4-2.png)

주문 애그리거트에서 루트 엔티티인 Order는JPA의 `@Entity`로 매핑한다.

```java
import javax.persistence.Entity;

@Entity
@Table(name = "purchase_order")
public class Order {
}
```

루트 엔티티 인 Order 클래스는 `@Embedded`를 이용해서 밸류 타입 프로퍼티를 설정한다.

``` java
@Entity
public class Order {

	...
	@Embedded
	private Orderer orderer;
	
	@Embedded
	private Shippinginfo shippinginfo;
	...
	
}
```


### 4.3.3 필드 접근 방식 사용

객체가 제공할 기능 중심으로 엔티티를 구현하도록 유도하려면 JPA 매핑 처리를 필드 방식으로 선택해서 불필요한 get/set 메서드를 구현하지 말아야 한다.

가령, 주문 취소 같은 걸 set메서드 대신 도메인 중심으로 orderCancle() 와 같은 메서드를 직접 선언해서 처리한다.

### 4.3.5 밸류 컬렉션: 별도 테이블매핑

Order 엔티티는 한 개 이상의 OrderLine을 가질 수 있다. OrderLine 에 순서가 있다면 다음과 같이 List 타입을 이용해서 컬렉션을 프로퍼티로 지정할 수 있다.

``` java
public class Order {

		private List<OrderLine> orderliness;
		...
}
```

Order와 OrderLine을 저장하기 위한 테이블은 ［그림 4.4］와 같이 매핑 가능하다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-4-3.png)

### 4.3.8 별도 테이블에 저장하는 밸류 매핑

루트 엔티티외 또 다른 엔티티가 있다면 진짜 엔티티인지 의심해보자.

단지 별도 테이블에 데이터를 저장한다고 해서 엔티티인 것은 아니다. 주문 애그리거트도 OrderLine 을 별도 테이블로 저장하지만 OrderLine 자체는 엔티티가 아니라 밸류이다.

자신만의 독자적인 라이프 사이클을 갖는다면 구분되는 애그리거트일 가능성이 높다.

애그리거트에 속한 객체가 밸류인지 엔티티인지 구분하는 방법은 고유 식별자를 갖는지 확인하는 것이다.

하지만 식별자를 찾을 때 매핑되는 테이블의 식별자를 애그리거트의 식별자와 동일한 것으로 착각하면 안된다. 별도의 테이블로 저장하고 테이블의 PK가 있다고 해서 테이블과 매핑되는 애그리거트 구성요소가 항상 고유 식별자를 갖는것은 아니다.

- 예를 들어, Article 과 ArticleContent가 있는데,
- 여기서 ArticleContent 테이블의 ID 컬럼이 식별자이므로 엔티티로 착각해서 Article과 1대1 연관으로 매핑할 수 있다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-4-4.png)

- 하지만 ArticleContent 는 Article의 내용을 담고 있는 밸류로 생각하는 것이 맞다.
- ID는 식별자이긴 하지만 이 식별자를 사용하는 이유는 Article 테이블의 데이터와 연결하기 위함이지 **ArticleContent 를 위한 별도 식별자가 필요하기 때문은 아니다.**
- 따라서 ArticleContent 를 밸류로 보고 접근하면 모델은 아래 그림과 같다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-4-5.png)


## 4.5 애그리거트의 영속성 전파

애그리거트가 완전한 상태여야 한다는 것은 애그리거트 루트를 조회하는 것 뿐만 아니라 저장하고 삭제, 수정할 때도 하나로 처리해야 함을 의미한다.

애그리거트에 속한 `@Entity` 타입에 대한 매핑은 cascade 속성을 사용해서 저장과 삭제 시에 함께 처리되도록 설정해야 한다.

@OneToOne, ©OneToMany 는 cascade 속성의 기본값이 없으므로 아래와 같이 cascade 속성값으로 `Cascade.PERSIST`, `CascadeType.REMOVE` 를 설정한다.

``` java
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
		orphanRemoval = true)
@JoinColumn(name = "product_id")
@OrderColumn(name = "list_idx")
priate List<Image> images = new ArrayListo<>();
```


## 4.7 도메인 구현과 DIP

구현 기술에 대한 의존 없이 도메인을 순수하게 의존하려면 스프링 데이터 JPA의 Repository 인터페이스를 상속받지 않도록 수정하고 아래 그림 4.9 와 같이 ArticleRepository 인터페이스를구현한 클래스를 인프라에 위치시켜야 한다.

또한 JPA에 특화된 애너테이션을 모두 지우고 인프라에 JPA를 연동하기 위해 클래스를 추가한다.

![](/book/Starting-with-Domain-Driven-Design/img/Starting-with-Domain-Driven-Design-4-6.png)

이러한 구조를 가지면 구현 기술을 변경하더라도 도메인이 받는 영향을 최소화할 수 있다.

DIP를 적용하는 주된 이유는 **저수준 구현이 변경되더라도 고수준이 영향을 받지 않도록 하기 위함**이다.

개인 생각
- 하지만, 만약 JPA와 같은 기술 외에 다른 기술로 대체할 것 같지 않는 환경이라면 DIP를 지키지 않아도 된다고 생각한다.
- DIP를 완벽히 지키는 것도 좋지만 현실에서는 품질과 일정 사이를 적절히 균형을 조절하면서 나아가야 하는 상황이 많기 때문에, 코드 품질을 80-90% 유지하도록 노력하자.
