# 자바 개발자를 위한 코틀린 입문(Java to Kotlin Starter Guide)

## 섹션1. 코틀린에서 변수와 타입, 연산자를 다루는 방법

### 1강. 변수 선언 키워드 - var과 val의 차이점

- var - variable(가변) / val - value(불변)
- 코틀린에서는 모든 변수에 수정 가능 여부(var / val)를 명시해주어야 한다.
- 타입, 초기값을 지정하지 않으면 컴파일 에러가 발생 ⇒ 지정해줘야 함
- 모든 변수는 우선 val(불변)으로 만들고 꼭 필요한 경우 var(가변)으로 변경한다.

- primitive type, reference type → 코틀린에서는 상황에 따라서 내부적으로 primitive type로 바꿔서 처리해준다.
- 즉, boxing/unboxing을 고려하지 않아도 되도록 코틀린이 알아서 처리해준다.

**정리**

- 모든 변수는 var / val을 붙여 주어야 한다.
    - var: 변경 가능하다. / val: 변경 불가능하다(read-only)
- 타입을 명시적으로 작성하지 않아도, 타입이 추론된다.
- Primitive Type과 Reference Type을 구분하지 않아도 된다.
- Null이 들어갈 수 있는 변수는 타입 뒤에 ? 를 붙여주어야 한다.
    - 아예 다른 타입으로 간주된다.
- 객체를 인스턴스화 할 때 new를 붙이지 않아야 한다.

### 2강. 코틀린에서 null을 다루는 방법

1. 코틀린에서 null 체크
2. safe call과 elvis 연산자

   safe call

    - ?. 를 쓰면 실행시키면 가능

    elvis 연산자 - 앞의 연산 결과가 null이면 뒤의 값을 사용
    
    - ?: 쓰면 앞이 null이면 뒤의 값(0)을 사용
        
3. 널 아님 단언!!
    - nullable type이지만, 아무리 생각해도 null이 될 수 없는 경우

    - 혹시나 null이 들어오면 null point exception이 나오기 때문에, 정말 null이 아닌게 확실한 경우에만 널 아님 단언!!을 사용해야 한다.
4. 플랫폼 타입
    - null 관련 정보를 알수 없을 때 사용

### 3강. 코틀린에서 Type을 다루는 방법

1. 기본타입 → 타입 변환을 `명시적` 으로 이루어저야 함 (자바는 `암시적`으로 변경됨)

   Byte
   Short**Int
   Long
   Float
   Double**
   부호 없는 정수들

2. 타입 캐스팅

    - 한번 코틀린 컴파일러가 컨텍스트를 분석해가지고 if 안에는 들어왔는데 if에서 타입 체크를 해줬음 → 그러면 이 타입으로 간주될 수 있겠구나라고 인지함
3. kotlin의 3가지 특이한 타입
    1. Any
        - **Java의 Object 역할**. 모든 객체의 **최상위 타입**
        - 모든 Primitive Type의 최상의 타입도 Any이다.
        - null을 포함하고 싶으면, `Any?` 로 표현
        - Any에 equals / hashCode / toString 존재
    2. Unit
        - **Java의 void와 동일한 역할**
    3. Nothing
        - **함수가 정상적으로 끝나지 않았다**는 사실을 표현하는 역할
        - 무조건 예외를 반환하는 함수 / 무한 루프 함수

       → 실제로 코딩에서 쓰진 않음

4. String Interpolation, String indexing

    - 변수 이름만 사용하더라도 `${변수}` 를 사용하는 것이 아래의 이유로 좋다.
        - 가독성
        - 일괄 변환
        - 정규직 활용
    - 문자열에서 특정 문자 가져오기 ⇒ *println*(str[0])

### 4강. 코틀린에서 연산자를 다루는 방법

1. 단항 연산자 / 산술 연산자 ⇒ 같음
2. 비교 연산자와 동등성, 동일성
    - 사용법은 Java와 같다.
    - Java와 다르게 객체를 비교할 때, 비교 연산자를 사용하면 자동으로 `compareTo`를 호출해준다.

    - 동등성: 두 객체의 값이 같은가
    - 동일성: 완전히 동일한 객체인가(주소값이 같은가)
3. 논리연산자 / 코틀린에 있는 특이한 연산자 → 반복문에서 자세히 배울 예정
    - in / !in
    - a..b
    - a[i]
    - a[i] = b
4. 연산자 오버로딩
    - 코틀린에서는 객체마다 연산자를 직접 정의할 수 있음

    ```kotlin
    fun main() {
        val money1 = Money(1_000L)
        val money2 = Money(2_000L)
        println(money1 + money2) // Money(amount=3000)
    }
    ```


## 섹션2. 코틀린에서 코드를 제어하는 방법

### 5강. 코틀린에서 조건문에서 다루는 방법

1. if-else문
2. Expression과 Statement
    - 자바는 Statement(프로그램의 문장, 하나의 값으로 도출되지 않음)
    - 코틀린은 Expression(하나의 값으로 도출되는 문장)
    - 코틀린에서는 if-else문을 expression으로 사용할 수 없기 때문에 3항 연산자가 없음
        - switch 대신 when을 써주고 return문을 맨 앞에 써줄 수 있음

    ```kotlin
    fun getPassOrFail(score: Int): String {
        return if (score >= 50) {
            "P"
        } else {
            "F"
        }
    }
    
    fun getGrade(score: Int): String {
        return if (score >= 90) {
            "A"
        } else if (score >= 80) {
            "B"
        } else if (score >= 70) {
            "C"
        } else {
            "D"
        }
    }
    ```

3. switch 과 when
    - 맨 앞에 return 문을 써준다.

    ```kotlin
    fun getGradeWithSwitch(score: Int): String {
        return when (score / 10) {
            9 -> "A"
            8 -> "B"
            7 -> "C"
            else -> "D"
        }
    }
    
    fun getGradeWithSwitch(score: Int): String {
        return when (score / 10) {
            in 90..99 -> "A"
            in 80..89 -> "B"
            in 70..79 -> "C"
            else -> "D"
        }
    }
    ```

    ```kotlin
    when (값) {
    	조건부 -> 어떠한 구문
      조건부 -> 어떠한 구문 
      else -> 어떠한 구문
    }
    ```

    - 조건부에서는 어떠한 expression이라도 들어갈 수 있다. (ex. is Type)

    ```kotlin
    fun getGradeWithSwitch(score: Int): String {
        return when (score / 10) {
            in 90..99 -> "A"
            in 80..89 -> "B"
            in 70..79 -> "C"
            else -> "D"
        }
    }
    ```

    - 값 → 값이 없을 수 있다.

    ```kotlin
    fun judgeNumber2(number: Int) {
        when {
            number == 0 -> println("주어진 숫자는 0입니다.")
            number % 2 == 0 -> println("주어진 숫자는 짝수입니다.")
            else -> println("주어지는 숫자는 홀수입니다.")
        }
    }
    ```


### 6강. 코틀린에서 반복문을 다루는 방법

1. for-each 문
2. 전통적인 for문
3. Progression과 Range
    - .. 연산자: 범위를 만들어 내는 연산자 → 등차수열을 만들어줌
    - ex. 1..5 step 2: 시작값 1, 끝값 5, 공차 2인 등차수열

4. while문

### 7장. 코틀린에서 예외를 다루는 방법

1. try catch finally 구문

   예시. 주어진 문자열을 정수로 변경하는 예제

2. checked exception, unchecked exception
    - 코틀린에서는 throw 구문이 없다는 점
        - Checked Exception과 UnChecked Exception을 구분하지 x
        - 모두 `UncheckException`으로 본다.
3. try with resources
    - 코틀린에서는 이러한 구문이 없지만, `use`라는 inline 확장함수를 사용해야 한다.

### 8장. 코틀린에서 함수를 다루는 방법

1. 함수 선언 문법
    - 함수는 클래스 안에, 파일 최상단에 있을 수 있다. 또한,  한 파일안에 여러 함수들이 있을 수 있다.
2. default parameter

    - 물론 코틀린에서도 자바와 동일하게 오버로드 기능은 있다.
3. named argument = 이름있는 파라미터

- Lecture8 - 결과

    ```kotlin
    package lec08
    
    fun main() {
        repeat("Hello World!")
        repeat("Hello World!", 3, true)
        repeat("Hello World!", useNewLine = false)
    }
    
    fun repeat(
        str: String, num: Int = 3, useNewLine: Boolean = true
    ) {
        for (i in 1..num) {
            if (useNewLine) {
                println(str)
            } else {
                print(str)
            }
        }
    }
    ```

- builder를 직접 만들지 않고 builder의 장점을 가지게 된다.

    ```kotlin
    fun main() {
        printNameAndGender(name = "MALE", gender = "팬시")
    }
    
    fun printNameAndGender(name: String, gender: String) {
        println(name)
        println(gender)
    }
    ```

1. 같은 타입의 여러 파라미터 받기 (가변인자)
    - 배열을 바로 넣는 대신 스프레드 연산자(*)을 붙여줘야 한다.

    ```kotlin
    package lec08
    
    fun main() {
        var array = arrayOf("A", "B", "C")
        printAll(*array)
    }
    
    fun printAll(vararg strings: String) {
        for (str in strings) {
            println(str)
        }
    }
    ```