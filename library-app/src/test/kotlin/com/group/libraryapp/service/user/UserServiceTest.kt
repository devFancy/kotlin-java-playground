package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor( // 밑에 @Autowired 생략 가능
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장이 정상 동작한다.")
    fun saveUserTest() {
        // given
        val request = UserCreateRequest("팬시", null)

        // when
        userService.saveUser(request)

        // then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("팬시")
        assertThat(results[0].age).isNull()
    }

    @Test
    @DisplayName("유저 조회가 정상 동작한다.")
    fun getUserTest() {
        // given
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null)
        ))

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2) // [UserResponse(0, UserResponse()]
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B") // ["A", "B"]
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    @DisplayName("유저 업데이트가 정상 동작한다.")
    fun updateUserNameTest() {
        // given
        val saveUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(saveUser.id!!, "B")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제가 정상 동작한다.")
    fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))

        // when
        userService.deleteUser("A")

        // then
        assertThat(userRepository.findAll().isEmpty())
    }

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다")
    fun getUserLoanHistoriesTest1() {
        // given
        userRepository.save(User("A", null))

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다.")
    fun getUserLoanHistoriesTest2() {
        // given
        val savedUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUser, "책1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "책2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED)
        ))

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name")
            .containsExactlyInAnyOrder("책1", "책2", "책3")
        assertThat(results[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true)
    }
}