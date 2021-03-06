package chapter6

import org.junit.Assert
import org.junit.Before
import org.junit.Test

//6.1. 널 가능성
//이 함수가 널을 인자로 받을 수 있는가? -> null을 리터럴로 사용하는 경우 + 변수나 식의 값이 실행 시점에 null이 될 수 있는 경우
//타입 이름 뒤에 물음표를 붙이면 그 타입의 변수나 프로퍼티에 null 참조를 저장할 수 있다는 뜻. 모든 타입은 기본적으로 널이 될 수 없는 타입이다.

//안전한 호출 연산자: ?.
class Employee(val name:String, val manager:Employee?)
fun managerName(employee:Employee): String? = employee.manager?.name

//안전한 호출 연쇄시키기
class Address(val streetAddress:String, val zipCode:Int, val city:String, val country:String)
class Company(val name:String, val address:Address?)
class Employee2(val name:String, val company:Company?)
fun Employee2.countryName() = this.company?.address?.country?:"Unknown" //안전한 호출 연산자 연쇄

//엘비스 연산자: ?:
fun printShippingLabel(employee:Employee2){
    val address = employee.company?.address?: throw IllegalArgumentException("No address") //주소가 null이면 예외 발생
    with(address){
        println(streetAddress)
        println("$zipCode $city, $country")
    }
}

//안전한 캐스트: as?
class Person3(val firstName: String, val lastName:String){
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? Person3 ?: return false //타입이 일치하지 않으면 false 반환.
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName //안전한 캐스트를 하고 나면 otherPerson이 Person3 타입으로 스마트캐스트 됨.
    }

    override fun hashCode(): Int =
        firstName.hashCode() * 37 + lastName.hashCode()

}

//null 아님 단언: !!
fun ignoreNulls(s:String?){
    val sNotNull:String = s!!
    println(sNotNull.length)
}

//let 함수. 자신의 수신 객체를 인자로 전달받은 람다에게 넘긴다.
//널이 될 수 있는 값을 널이 아닌 값만 인자로 받는 함수에 넘기는 경우 주로 사용한다. let을 쓰면 긴 식의 결과를 저장하는 변수를 따로 만들 필요 없다.
fun sendEmailTo(email:String){
    println("Sending email to $email")
}


//나중에 초기화할 프로퍼티
//코틀린에서는 클래스 안의 널이 될 수 없는 프로퍼티를 생성자 안에서 초기화하지 않고 특별한 메소드 안에서 초기화할 수 없다.
//lateinit 변경자를 붙이면 프로퍼티를 나중에 초기화할 수 있다.
//나중에 초기화하는 프로퍼티는 항상 var여야 한다.  val(final 필드로 컴파일, 반드시 생성자 안에서 초기화)
class MyService{
    fun performAction():String = "action"
}
class MyTest{
    private lateinit var myService:MyService //초기화하지 않고 널이 될 수 없는 프로퍼티로 선언
    @Before fun setUp(){
        myService = MyService() //진짜 초깃값 지정
    }

    @Test fun testAction(){
        Assert.assertEquals("action", myService.performAction()) //널 검사를 수행하지 않고 프로퍼티 사용
    }
}

//널이 될 수 있는 타입 확장
//널이 될 수 있는 수신 객체에 대해 확장 함수 호출하기
fun verifyInput(input:String?){
    if(input.isNullOrBlank()){ //안전한 호출을 하지 않아도 된다.
        println("Please fill in the required fields")
    }
}
//널이 될 수 있는 타입에 대한 확장을 정의할 때, 그 함수의 내부에서 this는 널이 될 수 있다. 따라서 명시적으로 널 여부를 검사한다.
fun String?.isNullOrBlank():Boolean=
    this==null || this.isBlank()

//타입 파라미터의 널 가능성. 코틀린에서는 함수나 클래스의 모든 타입 파라미터는 기본적으로 널이 될 수 있다.
//따라서 타입 파라미터 T를 클래스나 함수 안에서 타입 이름으로 사용하면 이름 끝에 물음표가 없더라도 T가 널이 될 수 있는 타입이다.
fun <T> printHashCode(t:T){
    println(t?.hashCode()) //t가 널이 될 수 있으므로 안전한 호출 사용. 안전한 호출 안하면 실행 안됨. 안전한 호출 하면 null 반환.
}
//타입 파라미터가 널이 아님을 확실히 하려면 널이 될 수 없는 타입 상한을 지정해야 한다. 이를 지정하면 널이 될 수 있는 값을 거부하게 된다.
fun <T> printHashCode2(t:T){ //이제 T는 널이 될 수 없는 타입.
    println(t.hashCode())
}

//널 가능성과 자바
/*
첫번째로 자바 코드에 애노테이션으로 표시된 널 가능성 정보를 활용한다. @Nullable String은 String?과, @NotNull String은 String과 같다.
애노테이션이 소스 코드에 없는 경우 자바의 타입은 코틀린의 플랫폼 타입이 된다.
플랫폼 타입은 코틀린이 널 관련 정보를 알 수 없는 타입을 말한다.
그 타입을 널이 될 수 있는 타입으로 처리하든, 널이 될 수 없는 타입으로 처리하든 개발자의 몫이며 컴파일러는 모든 연산을 허용한다.
*/

fun main(){
    //fun strLenSafe(s:String?) = s.length() null이 될 수 있는 타입의 변수에 대해 실행가능한 연산이 제한됨.
    val x:String? = null
    //var y:String = x null이 될 수 없는 타입의 변수에 null이 될 수 있는 값 대입 불가
    fun strLen(s:String) = s.length
    //strLen(x) null이 될 수 있는 타입의 값을 null이 될 수 없는 타입의 파라미터를 받는 함수에 전달 불가

    //안전한 호출 연산자 ?.
    val ceo = Employee("boss", null)
    val developer = Employee("smith", ceo)
    println(managerName(developer))
    println(managerName(ceo))

    //엘비스 연산자 ?:
    val address = Address("Elsetr. 47", 80808, "Munich", "Germany")
    val jetbrains = Company("Jetbrains", address)
    val employee = Employee2("Dmitry", jetbrains)
    printShippingLabel(employee)
    //printShippingLabel(Employee2("alexey", null))

    //안전한 캐스트 as?
    val p1 = Person3("Dmitry", "Jemerov")
    val p2 = Person3("Dmitry", "Jemerov")
    println(p1==p2)
    println(p1.equals(42))

    //null 아님 단언 !!
    //ignoreNulls(null) NPE 발생

    //let 함수
    var email:String? = "yole@example.com"
    email?.let{
        sendEmailTo(it)
    }
    email = null
    email?.let{ sendEmailTo(it) }

    //나중에 초기화할 프로퍼티
    val test = MyTest()
    test.setUp()
    println(test.testAction()) //반환값이 kotlin.Unit?

    //널이 될 수 있는 타입 확장
    verifyInput(" ")
    verifyInput(null)

    //타입 파라미터의 널 가능성
    printHashCode(null) //T의 타입은 Any?로 추론됨.
    //printHashCode2(null)  error: Null can not be a value of a non-null type TypeVariable(T)
}