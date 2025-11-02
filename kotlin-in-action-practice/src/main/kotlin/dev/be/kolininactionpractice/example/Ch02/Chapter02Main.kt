package dev.be.kolininactionpractice.example.Ch02

fun main() {
    println("Hello, world!")
    val result = max(10, 20)
    println(result)

    // 변수 - 예시 코드
    val nickname: String = "fancy"
    val nickname2 = "fancy"
    println(nickname)
    println(nickname2)


    // 문자열 템플릿
    val input = readln()
    val name = if (input.isNotBlank()) input else "Kotlin"
    println("Hello, $name!")
    println("Hello, ${name}!")

    // enum 과 when
    println(getColor(Color.YELLOW)) // Yellow
    println(getColor2(Color.YELLOW)) // YELLOW
    println(getColor3(Color.RED, Color.ORANGE, Color.YELLOW)) // RED ORANGE YELLOW

}

// 함수 - 예시 코드
fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}

// enum 과 when - 예시 코드
fun getColor(color: Color) =
    when(color) {
        Color.RED -> "Red"
        Color.ORANGE -> "Orange"
        Color.YELLOW -> "Yellow"
    }

fun getColor2(color: Color): String {
    return when (color) {
        Color.RED, Color.ORANGE, Color.YELLOW -> "${color.name}"
    }
}

fun getColor3(color1: Color, color2: Color, color3: Color) =
    when {
        (color1 == Color.RED || color2 == Color.ORANGE || color3 == Color.YELLOW) -> "RED ORANGE YELLOW"
        else -> throw RuntimeException()
    }
