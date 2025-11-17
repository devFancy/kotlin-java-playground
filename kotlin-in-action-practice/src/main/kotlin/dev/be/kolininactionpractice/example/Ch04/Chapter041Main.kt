package dev.be.kolininactionpractice.example.Ch04


// NOTE: 4.1
fun main() {
    val button = Button()
    button.showOff()
    // I'm clickable!
    // I'm a focusable!
    button.click() // I was clicked
    val themedButton = ThemedButton()
    themedButton.showOff() // I'm a themedButton!
}

// NOTE: 4.1.1
class Button : Clickable, Focusable {
    override fun click() = println("I was clicked")

    override fun showOff() {
        // 상위 타입의 구현을 호출할 때는 상위 타입의 이름을 <> 사이에 넣은 super를 사용한다.
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
    // 만약 상속한 구현 중 단 하나만 호출한다면 아래와 같이 작성한다.
    // override fun showOff() = super<Clickable>.showOff()
}

interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable!")
}

interface Focusable {
    fun showOff() = println("I'm a focusable!")
}

// NOTE: 4.1.2
open class RichButton : Clickable {
    fun disable() {} // 기본적으로 final 이기 때문에 하위 클래스가 이 메서드를 오버라이드할 수 없다.
    open fun animate() {} // open 변경자를 사용했기 때문에, 하위 클래스가 이 메서드를 오버라이드할 수 있다.
    override fun click() {} // 위와 동일.
}

class ThemedButton : RichButton() {
//    fun disable() {} // 상위 클래스가 오버라이드 할 수 없게 되어있기 때문에 사용할 수 없다.
    override fun animate() {}
    override fun click() {}
    override fun showOff() = println("I'm a themedButton!")
}

abstract class Animated {
    abstract val animationSpeed: Double // 추상 프로퍼티로 하위 클래스가 값을 제공할 필요가 있다.
    // 아래 2개의 프로터티는 추상이 아니기 때문에, 기본적으로 열려있지 않다. open 변경자를 통해 열게 할 수 있다.
    val keyframes: Int = 20
    open val frames: Int = 60

    abstract fun animate() // 추상 함수로, 구현이 없기에 하위 클래스에서 이 함수를 반드시 오버라이드해야 한다.
    // 아래 2개의 메서드는 추상이 아니기 때문에, 기본적으로 열려있지 않다. open 변경자를 통해 열게 할 수 있다.
    open fun shopAnimating() {}
    fun animateTwice() {}
}