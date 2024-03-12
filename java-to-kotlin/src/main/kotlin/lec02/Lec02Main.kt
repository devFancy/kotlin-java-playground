package lec02

fun main() {
    val str1: String? = "ABC"
    val str2: String? = null
    println(str1?.length)
    println(str2?.length)
    println(str2?.length ?: 0)

    print(startsWith("ABC"))
    var person = Person("공부하는 개발자")
    startsWithA(person.name)
}

fun startsWithA1(str: String?): Boolean {
//    return str?.startsWith("A")
//        ?: throw IllegalArgumentException("null이 들어왔습니다")

    if (str == null) {
        throw IllegalArgumentException("null이 들어왔습니다.")
    }
    return str.startsWith("A")
}

fun startsWithA2(str: String?): Boolean? {
//    return str?.startsWith("A")

    if (str == null) {
        return null
    }
    return str.startsWith("A")
}

fun startsWithA3(str: String?): Boolean {
//    return str?.startsWith("A") ?: false
    if (str == null) {
        return false
    }
    return str.startsWith("A")
}

fun startsWith(str: String?): Boolean {
    return str!!.startsWith("A")
}

fun startsWithA(str: String): Boolean {
    return str.startsWith("A")
}