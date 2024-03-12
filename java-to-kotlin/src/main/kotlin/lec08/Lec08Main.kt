package lec08

fun main() {
    var a = 1
    var b = 2
    println(max(a, b))
    println(max2(a, b))
}

fun max(a: Int, b: Int): Int {
    return if (a > b) {
        a
    } else {
        b
    }
}

fun max2(a: Int, b: Int): Int =
    if (a > b) {
        a
    } else {
        b
    }