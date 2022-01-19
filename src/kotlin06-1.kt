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

}