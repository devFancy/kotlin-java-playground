//package lec09
//
//fun main() {
//    val person = Person("팬시", 100)
//    println(person.name)
//    person.age = 10
//    println(person.age)
//
//    val person2 = JavaPerson("팬시", 100)
//    println(person2.name)
//    person2.age = 10
//    println(person2.age)
//
//    Person()
//}
//
//class Person0 constructor(name: String, age: Int) {
//    var name = name;
//    var age = age;
//}
//
//class Person(
//    var name: String,
//    var age: Int
//) {
//
//    init {
//        if (age <= 0) {
//            throw IllegalArgumentException("나이는 ${age}일 수 없습니다.")
//        }
//        println("초기화 블록")
//    }
//
//    constructor(name: String) : this(name, 1) {
//        println("첫 번째 부생성자")
//    }
//
//    constructor() : this("팬시") {
//        println("두 번째 부생성자")
//    }
//
//    // 방법1. 함수
//    fun isAdult(): Boolean {
//        return this.age >= 20
//    }
//
//    // 방법2. 프로퍼티
//    val isAdult: Boolean
//        get() {
//            return this.age >= 20
//        }
//}