package dev.be.kolininactionpractice.example.Ch02

interface Expr
// Num, Sum 에 있는 프로퍼티가 val 로 지정되어 있으므로 스마트 캐스팅을 지원한다.
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.left) + eval(e.right)
        else -> throw IllegalArgumentException()
    }

fun main() {
    val sum = Sum(Num(3), Num(2))
    // is 로 타입을 한 번 검사하면, 컴파일러가 '해당 변수에 대한 타입이 보장된다'고 판단하고 자동으로 타입을 변환해준다.
    if (sum.left is Num) {
        println(sum.left.value) // 결과: 3
    }
    if (sum.right is Num) {
        println(sum.right.value) // 결과: 2
    }

    println(eval(Sum(Num(3), Num(2)))) // 결과: 5
}
