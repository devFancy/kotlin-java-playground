package lec16

fun main() {
    val str = "ABC"
    println(str.lastChar())
}

fun String.lastChar(): Char {
    return this[this.length - 1] // this를 통해 인스턴스에 접근이 가능
}
