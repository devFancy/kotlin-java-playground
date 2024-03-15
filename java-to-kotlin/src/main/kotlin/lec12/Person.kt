package lec12

fun main() {
    println(Singleton.a)
    Singleton.a += 10
    println(Singleton.a)
}

class Person private constructor(
    var name: String,
    var age: Int,
) {
    companion object {
        private const val MIN_AGE = 1

        @JvmStatic
        fun newBaby(name:String): Person {
            return Person(name, MIN_AGE)
        }
    }
}

object Singleton {
    var a: Int = 0
}