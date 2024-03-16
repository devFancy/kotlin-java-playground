package lec15

fun main() {
    val map = mutableMapOf<Int, String>() // 타입을 추론할 수 없기에, 타입을 지정함
    map[1] = "MONDAY"
    map[2] = "TUESDAY"

    mapOf(1 to "MONDAY", 2 to "TUESDAY")

    for (key in map.keys) {
        println(key)
        println(map[key])
    }

    for ((key, value) in map.entries) {
        println(key)
        println(value)
    }
}