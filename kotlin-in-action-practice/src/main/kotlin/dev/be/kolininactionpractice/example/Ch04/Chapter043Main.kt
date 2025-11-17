fun main() {
    val chef = ExpertChef()
    val manager = Manager(chef)

    // Manager는 Cook의 기능을 직접 구현하지 않았지만,
    // by 키워드를 통해 위임받아 호출할 수 있다.
    println(manager.makePizza()) // 페퍼로니 피자
    println(manager.makePasta()) // 크림 파스타

    // Manager 고유의 기능도 호출 가능하다.
    manager.manageStaff()  // 직원들을 관리합니다.
}

interface Cook {
    fun makePizza(): String
    fun makePasta(): String
}

class ExpertChef : Cook {
    override fun makePizza() = "페퍼로니 피자"
    override fun makePasta() = "크림 파스타"
}

// 위임 클래스 - Delegator 패턴!
// 'Cook'의 역할은 수행해야 하지만, 실제 구현은 'chef'에게 맡긴다(by chef).
class Manager(val chef: ExpertChef) : Cook by chef {

    // makePizza(), makePasta()는 chef에게 자동 위임된다.
    // 코드를 작성할 필요가 없습니다.

    fun manageStaff() {
        // 매니저의 고유 기능
        println("직원들을 관리합니다.")
    }
}