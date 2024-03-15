package lec12;

public class Lec12Main {

    public static void main(String[] args) {
        Person.Companion.newBaby("ABC"); // 일반적인 형태(이름이 없는 경우)
        Person.newBaby("ABC"); // Kotlin 코드에서 메서드 위에 @JvmStatic을 붙여줘야 함
        // Person.Factory.newBaby("ABC"); // companion object 뒤에 Factory 라는 이름이 있을 경우
    }

}
