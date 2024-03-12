package lec01

fun main() {
    var number1: Long = 10L // (1) Variable: non-final
    val number2: Long = 10L // (2) Value: final

    val number3 = 1_000L // (3) not null
    var number4: Long? = null // nullable
    number4 = 1_000L

    println(number3.javaClass)
    println(number4.javaClass)

    var person = Person("devfancy") // (4)
    println(person.name)
}