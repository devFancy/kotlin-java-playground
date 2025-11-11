package dev.be.kolininactionpractice.example.Ch03


fun main() {
    // NOTE: 3.1절
    val strings = listOf("first", "second", "fancy")
    println(strings.last()) // fancy

    println(strings.shuffled()) // [fancy, second, first]

    val  numbers = setOf(1, 10, 100)
    println(numbers.sum()) // 111

    // NOTE: 3.3절
    println("Kotlin".lastChar()) // n
    println("Kotlin".lastChar2()) // n

    // NOTE: 3.3.4절 - 확장 함수는 클래스의 멤버가 아니기 때문에 오버라이드가 불가능하다.
    val view: View = Button() // 변수의 '타입'은 View, '실제 객체'는 Button -> 컴파일에 변수의 타입이 호출된다.
    view.showOff() // I'm a view!

    // NOTE: 3.5
    val ip = "192.168.0.1"

    // 단순 문자열로 분리
    val partsString = ip.split(".")
    println("단순 문자열 분리: $partsString") // [192, 168, 0, 1]

    // 모든 문자로 쪼갤 경우 Regex 로 만든다.
    val partsRegex = ip.split(Regex("\\."))
    println("정규식으로 분리: $partsString") // [192, 168, 0, 1]


}

fun String.lastChar() : Char = this.get(this.length - 1)
// String -> 수신 객체 타입(receiver type), this -> 수신 객체(receiver object)

fun String.lastChar2(): Char = get(length -1)

open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() = println("Button clicked")
}

fun View.showOff() = println("I'm a view!")
fun Button.showOff() = println("I'm a Button!")