package lec14

fun main() {
    val person1 = PersonDto("팬시", 100)
    val person2 = PersonDto("팬시", 100)

    println(person1 == person2)
    println(person1)
}

data class PersonDto(
    val name: String,
    val age: Int
)