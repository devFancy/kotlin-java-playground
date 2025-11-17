fun main() {
    MyClass.callMe() //Companion object called!

    val myObject = MyClass()
//    myObject.callMe() // Error: Unresolved reference: callMe
}

// Note: 4.4.1
class MyClass {
    companion object {
        fun callMe() {
            println("Companion object called!")
        }
    }
}

// Note: 4.4.3
interface MouseListener {
    fun onEnter()
    fun onClick()
}