fun main() {
    // 1부터 10까지 출력
    for (i in 1..10) {
        println(i) // 결과: 1, 2, 3, ..., 10
    }

    // 1부터 10까지 2씩 증가
    for (i in 1..10 step 2) {
        println(i) // 결과: 1, 3, 5, 7, 9
    }

    // 10부터 1까지 2씩 감소
    for (i in 10 downTo 1 step 2) {
        print("$i, ") // 결과: 10, 8, 6, 4, 2
    }

    // map의 key, value를 for문으로 풀어낼 수 있다.
    for ((key, value) in mutableMapOf(Pair("A", 1))) {
        println("$key: $value")
    }

    // withIndex를 활용하면 리스트의 index, 값을 가져올 수 있다.
    for ((index, value) in mutableListOf(1, 2, 3).withIndex()) {
        println("$index: $value")
    }

    // in 연산자로 범위의 원소 검사를 할 수 있다.
    println(isLetter('q')) // 결과: true
    println("K" in "A".."Z" ) // 결과: true

}

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
