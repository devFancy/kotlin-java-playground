package dev.be.kolininactionpractice.example.Ch03

class User(val id: Int, val name: String, val address: String) {

//       ver1
//    // 한 필드를 검증하는 로컬 함수를 정의한다.
//    fun saveUser(user: User) {
//        fun validate(user: User,
//                     value: String,
//                     filedName: String) {
//            if (value.isEmpty()) {
//                throw IllegalArgumentException(
//                        "Can't save user ${user.id}: empty $filedName"
//                )
//            }
//        }
//
//        // 로컬 함수를 호출해서 각 필드를 검증한다.
//        validate(user, user.name, "Name")
//        validate(user, user.address, "Address")
//    }

//      ver2
//    fun saveUser(user: User) {
//        // saveUser 함수의 user 파라미터를 중복 사용하지 않는다.
//        fun validate(value: String,
//                     filedName: String) {
//            if (value.isEmpty()) {
//                // 바깥 함수의 파라미터에 직접 접근할 수 있다.
//                throw IllegalArgumentException(
//                        "Can't save user ${user.id}: " + "empty $filedName"
//                )
//            }
//        }
//
//        // 로컬 함수를 호출해서 각 필드를 검증한다.
//        validate(user.name, "Name")
//        validate(user.address, "Address")
//    }

    // ver3
    fun User.validateBeforeSave() {
        fun validate(value: String,
                     filedName: String) {
            if (value.isEmpty()) {
                // User의 프로퍼티를 직접 사용할 수 있다.
                throw IllegalArgumentException(
                        "Can't save user $id: empty $filedName"
                )
            }
        }

        validate(name, "Name")
        validate(address, "Address")
    }
}