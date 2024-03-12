package lec03

import java.util.Objects

fun main() {
    val number1 = 3
    val number2: Long = number1.toLong()

    val number3: Int? = 3
    val number4: Long = number3?.toLong() ?: 0L
}