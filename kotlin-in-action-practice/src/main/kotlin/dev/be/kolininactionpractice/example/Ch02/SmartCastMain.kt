package dev.be.kolininactionpractice.example.Ch02

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

// 스마트 캐스팅을 지원한다.
fun eval(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.left) + eval(e.right)
        else -> throw IllegalArgumentException()
    }

/**
 *  클래스의 프로퍼티를 스마트 캐스팅하고 싶다면 val 이면서 커스텀 접근자가 정의되어 있지 않아야 한다.
 *  - var거나 커스텀 접근자가 있으면 언제나 같은 타입을 반환해준다는 것을 확신할 수 없기 때문에..
 */
fun main() {
    val sum = Sum(Num(3), Num(2))
    // 스마트 캐스팅 가능
    if (sum.left is Num) {
        println(sum.left.value)
    }
    if (sum.right is Num) {
        println(sum.right.value)
    }

    println(eval(Sum(Num(3), Num(2)))) // 결과: 5
}
