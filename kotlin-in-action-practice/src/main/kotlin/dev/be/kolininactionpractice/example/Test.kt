package dev.be.kolininactionpractice.example

fun main() {
    var age = 10
    val user = User(age) // user.age = 10+1 = 11

    user.increaseAgeField(user.age)
    // 원본 변수를 변경하기 위해 반드시 반환 값을 재할달해야 한다.
    age = increase(age)

    println("user.age = ${user.age}") //12
    println("age = $age") //11
}

private fun increase(age: Int): Int {
    return age + 1 // age = 10+1 = 11
}

class User(age: Int) {
    var age: Int = age + 1

    fun increaseAgeField(age: Int) {
        this.age = age + 1 // 현재 user.age (11)에 +1 하여 12로 만듦
    }
}