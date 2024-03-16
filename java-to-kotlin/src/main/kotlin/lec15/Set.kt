package lec15

fun main() {
    val numbers = setOf(100, 200) //  불변 집합, <Int> 생략 가능
    val numbers2 = mutableSetOf(100, 200) // 가변 집합, 기본 구현체는 LinkedHashSet

    for (number in numbers) {
        println(number)
    }

    for ((index, number) in numbers.withIndex()) {
        println("$index $number")
    }
}
