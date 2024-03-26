## Kotlin + Spring

> 아래의 글은 [실전! 코틀린과 스프링 부트로 도서관리 애플리케이션 개발하기(Java 프로젝트 리팩터링)](https://www.inflearn.com/course/java-to-kotlin-2/dashboard) 에서 설명한 내용을 정리한 글입니다.

### Kotlin과 JPA를 함께 사용할 때 이야기 거리 3가지

#### setter에 관한 이야기

User 클래스에는 생성자 안의 var 프로퍼티가 있어 setter를 사용할 수 있지만, setter 대신 `updateName()` 이라는 추가적인 함수가 구현되어 있다.

```Kotlin
@Entity
class User (

    var name: String,

    val age: Int?,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }
    // ...
}
```

이는 밖에서 setter를 호출하는 것보다, 좋은 이름의 함수를 사용하는 것이 훨씬 좋은 코드이기 때문이다.

현재 name에 대한 setter는 public으로 열려있기 때문에 유저 이름 업데이트 기능에서 setter를 사용할 '수도' 있다.

setter를 완전히 막을 수 있는 방법은 2가지다.

첫 번째 방법은 `backing property`를 사용하는 것이다.

```kotlin
class User(
  private var _name: String
){
    val name: String
        get() = this._name
}
```

이렇게 `_name` 이라는 프로퍼티를 만들고, 읽기 전용으로 추가 프로퍼리 `name`을 만든다.

두 번째 방법은 `custom setter`를 사용하는 것이다.

```kotlin
class User(
    name: String // 프로퍼티가 아닌, 생성자 인자로만 name을 받는다.
) {
    var name = name
        private set
}
```

User의 생성자에서 name을 프로퍼티가 아닌, 생성자 인자로만 받고 이 name을 변경 가능한 name 프로퍼티로 넣어주되, name 프로퍼티에 private setter를 달아두는 것이다.

위의 두 가지 방법 모두 괜찮지만, 프로퍼티가 많아지면 번거로운 단점이 있다.

(필자의 생각) setter를 열어 두되 사용하지 않는 방법을 선호한다. 결국은 Trade-Off의 영역이라 생각하고, 회사에서 팀 간의 컨벤션을 잘 맞추는 것이 중요하지 않을까 싶다.

#### 생성자 안의 프로퍼티, 클래스 body 안의 프로퍼티

User 클래스 주 생성자 안에 `userLoanHistories`와 `id`는 꼭 주생성자 안에 있을 필요가 없다. 아래와 같이 코드가 바뀔 수 있다.

```kotlin
@Entity
class User (

    var name: String,

    val age: Int?,
    
) {

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
            
    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }
    // ...
}
```

위 코드도 잘 동작한다.

(필자의 생각) 어느 방법이든 상관없기 때문에, 명확한 가이드가 있다면 그에 따라 개발하는 것이 중요하다.

1. 모든 프로퍼티를 생성자에 넣는다.
2. 프로퍼티를 생성자 혹은 클래스 body 안에 구분해서 넣는다.

#### JPA와 data class

결론부터 말하면, JPA Entity는 **data class를 피하는 것이 좋다.**

data class는 equals, hashCode, toString 등의 함수를 자동으로 만들어준다. 이는 연관관계 상황에서 문제가 될 수 있다.

예를 들어, 현재 프로젝트에 있는 User와 UserLoanHistory는 일대다 관계를 맺고 있는 상황이다. 
여기서 User 쪽에 `equals()` 가 호출된다면, User는 본인과 관계를 맺고 있는 UserLoanHistory의 `equals()` 를 호출하게 되고, 
다시 UserLoanHistory는 본인과 관계를 맺고 있는 User의 `equals()`를 호출하게 된다. => 무한 순환 참조 발생 !

이런 상황으로 인해 JPA Entity는 data class를 피하는 것이 좋다.

#### Kotlin Class에서의 Domain

Entity (Class) 가 생성되는 로직을 찾고 싶을 경우, `constructor` 지시어를 명시적으로 작성하고 추적하면 편하다.

<img src="/img/kotlin-spring-section2-1.png">
