package lec15

fun main() {
    val numbers = listOf(100, 200) //  불변 리스트, <Int> 생략 가능
    val numbers2 = mutableListOf(100, 200) // 가변 리스트, 기본 구현체는 ArrayList
    numbers2.add(300)
    val emptyList = emptyList<Int>()

    printNumbers(emptyList())


    // List
    println(numbers[0]) // 첫번째 값 가져오기

    for (number in numbers) {
        println(number)
    }

    for ((idx, value) in numbers.withIndex()) {
        println("${idx} ${value}")
    }
}

private fun printNumbers(numbers: List<Int>) {

}