package chapter6
//코틀린의 원시 타입
//원시 타입: Int, Boolean 등
//자바는 참조 타입이 필요한 경우 특별한 래퍼 타입으로 원시타입 값을 감싸서 사용한다. Collection<Integer> 등.
//코틀린은 원시 타입과 래퍼 타입을 구분하지 않으므로 항상 같은 타입을 사용한다.
val i:Int = 1
val list:List<Int> = listOf(1,2,3)
//코틀린에서는 숫자 타입 등 원시 타입의 값에 대해 메소드를 호출할 수 있다.
fun showProgress(progress: Int){
    val percent = progress.coerceIn(0, 100)
    println("We are $percent% done!")
}

//널이 될 수 있는 원시 타입: Int?, Boolean? 등
//널 참조 - 자바의 참조 타입 변수에만 대입 가능. 코틀린에서 널이 될 수 있는 원시 타입 -> 자바의 래퍼 타입으로 컴파일.
data class Person(val name:String, val age:Int?=null){
    fun isOrderThan(other:Person): Boolean?{
        if( age==null || other.age==null ) //age는 자바에서 java.lang.Integer로 저장된다.
            return null
        return age > other.age
    }
}

//숫자 변환
//코틀린은 한 타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않는다.
val j = 1
//val l:Long = j type mismatch 컴파일 오류
val l:Long = j.toLong() //직접 변환 메소드를 호출해야 한다.

//nothing 타입. 함수가 정상적으로 끝나지 않는다는 사실을 알면 유용할 때가 있다.
fun fail(message: String):Nothing{
    throw IllegalArgumentException(message)
}


fun main() {
    //원시 타입
    showProgress(146)

    //널이 될 수 있는 원시 타입
    println(Person("Sam", 35).isOrderThan(Person("Amy", 42)))
    println(Person("Sam", 35).isOrderThan(Person("Amy")))

    //숫자 변환
    //박스 타입을 비교하는 경우, 두 박스 타입 간의 equals 메소드는 그안에 들어있는 값이 아니라 박스 타입 객체를 비교한다.
    val x = 1
    println(x.toLong() in listOf(1L, 2L, 3L)) //명시적 변환을 해줘야 true. 묵시적 변환은 false.

    //nothing 반환
    //fail("Error occurred")

}
