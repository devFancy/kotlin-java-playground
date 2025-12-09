package dev.be.kolininactionpractice.example.Ch07



fun main() {
    //ignoreNulls(null)

    var email: String? = "junyong@example.com"
    // email이 null이 아니므로 블록 실행 -> sendEmailTo 호출됨
    email?.let { sendEmailTo(it)}

    email = null
    // email이 null이므로 let 블록 자체가 실행되지 않음 -> 아무 일도 안 일어남
    email?.let { sendEmailTo(it) } // null이므로 호출 안함
}

// NOTE: 7.7
fun ignoreNulls(str: String?) {
    // "str은 절대 null이 아니야"라고 컴파일러에게 알린다.
    val strNotNull: String = str!!

    // 하지만 실제 값이 null이므로, 위 줄에서 NullPointerException(NPE)이 발생한다.
    println(strNotNull.length)
}

// NOTE: 7.9
fun sendEmailTo(email: String) {
    println("Sending email to $email")
}