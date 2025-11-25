package dev.be.kolininactionpractice.example.Ch05


fun main() {
    println("common: " + alphabet())
    println("with(CASE 1): " + alphabet2())
    println("with(CASE 2): " + alphabet3())
    println("with(CASE 3): " + alphabet4())
    println("apply: " + alphabet5())

    val names = listOf("mjy", "fancy", "junyong")
    val uppercaseNames = mutableListOf<String>()
    val reversedLongNames = names
            .map { it.uppercase() }
            .also { uppercaseNames.addAll(it) }
            .filter { it.length > 5 }
            .also { println(it) }
            .reversed()

    println("uppercaseNames: $uppercaseNames") // result: uppercaseNames: [MJY, FANCY, JUNYONG]
    println("reversedLongNames: $reversedLongNames") // reversedLongNames: [JUNYONG]
}

fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z')
        result.append(letter)
    result.append("\nNow I know the alphabet!")
    return result.toString()
}

// NOTE: with를 사용해 알파벳 만든 경우
fun alphabet2(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            this.append(letter)
        }
        this.append("\nNow I know the alphabet!")
        this.toString()
    }
}

// NOTE: with를 사용해 알파벳 만든 경우 - this 를 생략할 수 있다.
fun alphabet3(): String {
    val stringBuilder = StringBuilder()
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\nNow I know the alphabet!")
        toString()
    }
}

// NOTE: with를 사용해 알파벳 만든 경우 - 불필요한 stringBuilder 변수를 없앤 경우
fun alphabet4() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
    toString()
}

fun alphabet5() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
    toString()
}