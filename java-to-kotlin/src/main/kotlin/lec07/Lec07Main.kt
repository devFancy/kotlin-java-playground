package lec07

import java.lang.NumberFormatException

// 주어진 문자열을 정수로 변경하는 예제
fun parseIntOrThrow(str: String): Int {
    try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("주어진 ${str}는 숫자가 아닙니다")
    }
}

fun parseIntOrThrow2(str: String): Int? {
    return try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        null
    }
}
