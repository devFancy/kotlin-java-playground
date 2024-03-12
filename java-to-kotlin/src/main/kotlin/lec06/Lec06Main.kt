package lec06

fun main() {
    // for each
    var numbers = listOf(1L, 2L, 3L);
    for (number in numbers) {
        println(number)
    }

    for (i in 1..3) {
        println(i)
    }

    // 1씩 감소
    for (i in 3 downTo 1) {
        println()
    }

    // 2씩 증가
    for (i in 1..5 step 2) {
        println(i)
    }
}