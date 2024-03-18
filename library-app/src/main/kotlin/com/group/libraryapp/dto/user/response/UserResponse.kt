package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User

data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int?,
) {

    companion object { // 방법1. 정적 팩터리 메서드 방식
        fun of(user: User) : UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                age = user.age
            )
        }
    }
    // 방법2. 부생성자
//    constructor(user: User): this(
//        id = user.id!!,
//        name = user.name,
//        age = user.age
//    )
}
