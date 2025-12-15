package dev.be.kolininactionpractice.example.Ch08

fun main() {
    //fail("Error occured")
    // Exception in thread "main" java.lang.IllegalStateException: Error occured
    val letters = Array<String>(26) {
        i -> ('a' + i).toString()
    }
    println(letters.joinToString("")) // abcdefghijklmnopqrstuvwxyz
}

fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}

// NOTE: 8.12