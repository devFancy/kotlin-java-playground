package dev.be.kolininactionpractice.example.Ch04

import java.net.URI

// NOTE: 4.2
fun main() {
    val fancy = User("fancy")
    println(fancy.isSubscribed) // true

    val junYong = User("junYong", false)
    println(junYong.isSubscribed) // false


}


// NOTE: 4.2.1
class User(val nickname: String, val isSubscribed: Boolean = true)


// NOTE: 4.2.2
open class Downloader {
    constructor(url: String?)

    constructor(uri: URI?)
}

class MytDownloader : Downloader {
    constructor(url: String?) : super(url) { // 상위 클래스의 생성자를 호출한다.
    }
//    constructor(url: String?) : this(URI(url)) {} // 같은 클래스의 다른 생성자에게 위임한다.

    constructor(uri: URI?) : super(uri) {
    }
}