package lec11

open class Cat protected constructor(

)

open class Cat2 internal constructor(

)

open class Cat3 private constructor(

)

class Car(
    internal val name:String,
    private var owner: String,
    _price: Int
) {
    var price = _price
        private set
}