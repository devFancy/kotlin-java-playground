package dev.be.kolininactionpractice.example.Ch07


fun main() {
    // NOTE: 7.2
    val ceo = Employee("junyong", null)
    val developer = Employee("fancy", ceo)

    println(managerName(developer)) // junyong
    println(managerName(ceo)) // null

    // NOTE: 7.4
    println(strLenSafe("abc")) // 3
    println(strLenSafe(null)) // 0

    // NOTE: 7.5
    val address = Address("송도국제대로", 21986, "인천광역시", "대한민국")
    val kakaobank = Company("Kakaobank", address)
    val person = Person("junyong", kakaobank)

    printShippingLabel(person)
    // 송도국제대로
    // 21986 인천광역시 대한민국

    printShippingLabel(Person("fancy", null)) // 예외 발생!! (IllegalArgumentException)
}

// NOTE: 7.2
class Employee(val name: String, val manager: Employee?)

fun managerName(employee: Employee): String? = employee.manager?.name

// NOTE: 7.4
fun strLenSafe(s: String?): Int = s?.length ?: 0


// NOTE: 7.5
class Address(val streetAddress: String, val zipCode: Int, val city: String, val country: String)
class Company(val name: String, val address: Address?)
class Person(val name: String, val company: Company?)

fun printShippingLabel(person: Person) {
    val address = person.company?.address
        ?: throw IllegalArgumentException("No address")
    // 위에서 null 체크를 통과했으므로, 여기부터 address는 null이 아님을 보장받음
    // with를 썼을 때 (this가 address가 됨 -> 변수명 생략 가능)
    with(address) {// address는 null이 아니다.
        println(streetAddress)
        println("$zipCode $city $country")
    }
    // with를 안 썼을 때 (address 변수명을 계속 써야 함 -> 반복 발생)
    println(address.streetAddress)
    println("${address.zipCode} ${address.city} ${address.country}")
}