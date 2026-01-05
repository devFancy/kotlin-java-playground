package dev.be.kolininactionpractice.example.CH10

// NOTE 10.9 ~ 10.13

data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS {
    WINDOWS, LINUX, MAC, IOS, ANDROID
}

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID),
)

//val averageWindowsDuration = log
//    .filter { it.os == OS.WINDOWS }
//    .map(SiteVisit::duration)
//    .average()

//fun List<SiteVisit>.averageDurationFor(os: OS) =
//    filter { it.os == os }
//        .map(SiteVisit::duration).average()

// 핵심: '어떤 조건으로 필터링할 것인가'라는 행동을 인자로 받는다.
// (SiteVisit) -> Boolean 타입의 함수를 인자로 받는 `고차 함수`
fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
    filter(predicate)
        .map(SiteVisit::duration)
        .average()


fun main() {
//    println(log.averageDurationFor(OS.WINDOWS)) // 23.0
//    println(log.averageDurationFor(OS.MAC)) // 22.0

//    val averageMobileDuration = log
//        .filter { it.os in setOf(OS.IOS, OS.ANDROID) }
//        .map(SiteVisit::duration)
//        .average()
//    println(averageMobileDuration) // 12.15

    // 특정 OS(WINDOWS) 필터링 로직을 주입
    println(log.averageDurationFor { it.os == OS.WINDOWS })

    // 모바일 OS(IOS, ANDROID) 필터링 로직을 주입
    val averageMobileDuration = log.averageDurationFor {
        it.os in setOf(OS.IOS, OS.ANDROID)
    }
    println(averageMobileDuration) // 12.15

    // 특정 경로("/") 필터링 로직을 주입
    println(log.averageDurationFor { it.path == "/" }) // 24.1
}
