package lec05

fun validateScoreIsNotNegative(score: Int) {
    if(score < 0) {
        throw IllegalArgumentException("${score}는 0보다 작을 수 없습니다")
    }
}