package dev.be.kolininactionpractice.example.Ch05


fun main() {
    val people = listOf(Person("fancy", 29), Person("junyong", 30))
    // NOTE: 가장 큰 값을 반환
    println(people.maxByOrNull { it.age }) // result: Person(name=junyong, age=30)

    println(people.maxByOrNull({ p: Person -> p.age }))
    println(people.maxByOrNull() { p: Person -> p.age })
    println(people.maxByOrNull { p: Person -> p.age })

    // NOTE: 규칙 1 스타일 - 마지막 인자가 람다이므로 밖으로 뺌
    println(people.joinToString(" ") { it.name }) // result: fancy junyong

    // NOTE: 규칙2 스타일 - 인자가 많아 헷갈린다면 괄호 안에 이름을 붙여 명시
    val names = people.joinToString (
            separator = " ",
            transform = { p: Person -> p.name }
    )
    println(names) // result: fancy junyong

    val errors = listOf("403 Forbidden", "404 Not Found")
    printMessagesWithPrefix(errors, "Error: ")
    // result
    // Error:  403 Forbidden
    // Error:  404 Not Found


    // 1. 기존 방식 (클래스::멤버)
    val p = Person("fancy", 29)
    val ageFunction = Person::age
    println(ageFunction(p))
    // result: 29

    // 2. 바운드 멤버 참조 (인스턴스::멤버) -> 대상이 p로 고정됨
    val myageFunction = p::age
    println(myageFunction()) // 실행할 때 인자가 필요 없음! (이미 p인 걸 아니까)
    // result: 29

    createAllDoneRunnable().run() // result: All done!
}

data class Person(val name: String, val age: Int)

fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}


fun createAllDoneRunnable(): Runnable {
    return Runnable { println("All done!")}
}