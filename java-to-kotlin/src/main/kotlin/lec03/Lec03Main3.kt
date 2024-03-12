package lec03

fun main() {
    val person = Person("팬시", 27)
    println("이름 : ${person.name}이고 나이는 만${person.age}세 입니다.")

    val name = "팬시";

    val str = """
        ABC
        EFG
        ${name}
    """.trimIndent()
    print(str)

    var str2 = "ABC"
    println(str[0])
    println(str[2])
}