//3.1. 코틀린에서 컬렉션 만들기. 코틀린 컬렉션은 자바 컬렉션과 똑같은 클래스이다.
val strings = listOf("first", "second", "third")
val numbers = setOf(1,2,3,4,5)

//3.2. 함수를 호출하기 쉽게 만들기
fun <T> joinToString(
    collection: Collection<T>,
    separator: String=", ", //디폴트 파라미터 이용. -> 오버로드 중 상당수를 피할 수 있다.
    prefix: String="",
    postfix: String=""
):String{
    val result = StringBuilder(prefix)
    for((index, element) in collection.withIndex()){
        if(index>0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티
//코틀린에서는 함수를 직접 소스 파일의 최상위 수준, 모든 다른 클래스의 밖에 위치시키는 것이 가능.
//최상위 프로퍼티를 이용해 코드에 상수를 추가할 수 있다.
const val UNIX_LINE_SEPARATOR="\n" // 자바의 public static final String ~과 동일.

//3.3. 확장 함수와 확장 프로퍼티
//확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수이다.
fun String.lastChar(): Char = this.get(this.length-1) //수신 객체 타입: String, 수신 객체(확장 함수가 호출되는 대상): this
fun String.firstChar(): Char = get(0) //this없이 접근 가능. 확장 함수 안에서는 클래스 내부에서만 사용할 수 있는 private, protected 멤버를 사용할 수 없다.

//확장함수로 유틸리티 함수 정의
fun <T> Collection<T>.joinToStringUtil(
    separator: String=", ", //디폴트 파라미터 이용. -> 오버로드 중 상당수를 피할 수 있다.
    prefix: String="",
    postfix: String=""
):String{
    val result = StringBuilder(prefix)
    for((index, element) in this.withIndex()){ //수신 객체 this - collection
        if(index>0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//확장 함수는 오버라이드 할 수 없다. 코틀린은 호출될 확장 함수를 정적으로 결정하기 때문이다.
open class View{
    open fun click() = println("View clicked")
}
class Button: View(){
    override fun click()=println("Button clicked")
}
fun View.showoff() = println("I'm a view!")
fun Button.showoff() = println("I'm a button!") //확장함수는 오버라이딩 불가

//변경 가능한 확장 프로퍼티 선언
var StringBuilder.lastChar2: Char
    get() = get(length-1) // 프로퍼티 게터
    set(value: Char){
        this.setCharAt(length-1, value) //프로퍼티 세터
    }

//3.4. 컬렉션 처리
//가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의. vararg
fun together(args:Array<String>){
    val list = listOf("args: ", *args) //* 스프레드 연산자. 배열의 내용을 펼쳐서 각 원소가 인자로 전달되게 한다.
    println(list)
}

//중위 호출
//함수에 중위 호출을 허용하려면 infix 변경자를 선언 앞에 추가한다.
//infix fun Any.to(other:Any) = Pair(this, other) - to의 수신 객체는 제네릭함.

//3.6. 로컬함수와 확장 - 코드 다듬기
class User(val id: Int, val name:String, val address:String)
fun saveUser(user: User){
    if(user.name.isEmpty()){
        throw IllegalArgumentException("Empty name")
    }
    if(user.address.isEmpty()){
        throw IllegalArgumentException("Empty address") //필드 검증이 중복됨.
    }
}

//로컬함수 사용해 코드 중복 줄이기
fun saveUser2(user:User){
    fun validate(value:String, fieldName:String){
        if(value.isEmpty()){
            throw IllegalArgumentException("Empty $fieldName")
        }
    }
    validate(user.name, "Name")
    validate(user.address, "Address")
}

//검증 로직을 확장 함수로 추출하기
fun User.validateBeforeSave(){
    fun validate(value:String, fieldName:String){
        if(value.isEmpty()){
            throw IllegalArgumentException("empty $fieldName")
        }
    }
    validate(name, "Name")
    validate(address, "Address")
}

fun SaveUser3(user:User){
    user.validateBeforeSave()
}

fun main() {
    println(strings.last())
    println(numbers.maxOrNull())

    val list = listOf(1, 2, 3)
    println(joinToString(list, ";", "(", ")")) // 이름 붙인 인자 사용
    println(joinToString(list, ":"))

    println("Kotlin".lastChar()) //수신 객체: "Kotlin"
    println(list.joinToStringUtil(",", "{", "}")) //확장함수 호출

    val view: View = Button() //View 타입의 변수 선언 후 button() 대입
    view.showoff() // I'm a view 호출
    // 확장함수를 호출할 때, 수신 객체로 지정한 변수의 정적 타입(View)에 의해 어떤 확장 함수가 호출될 지 결정되지,
    // 그 변수에 저장된 객체의 동적인 타입(Button)에 의해 확장함수가 결정되지 않는다.

    val sb = StringBuilder("Kotlin?")
    sb.lastChar2 = '!'
    println(sb)

    val languages = arrayOf("java", "c++", "kotlin", "python")
    together(languages)

    //중위 호출과 구조 분해 선언
    1.to("one") //to 메소드를 일반적인 방식으로 호출함.
    1 to "one" //to 메소드를 중위 호출 방식으로 호출함.
    //인자가 하나뿐인 일반 메소드나 인자가 하나뿐인 확장함수에 중위호출을 이용할 수 있다.

    val (number, name) = 1 to "one" //구조 분해 선언. Pair의 내용으로 두 변수를 즉시 초기화한다.

    //3.5. 문자열과 정규식
    //자바에서는 split의 구분 문자열이 정규식. .의 경우 모든 문자를 나타내는 정규식으로 해석되어 문자열을 .으로 split하지 못함.
    //코틀린에서는 정규식을 파라미터로 받는 함수는 String이 아닌 Regex 타입의 값을 받음.
    println("12.345-6.A".split("\\.|-".toRegex()))
    println("12.345-6.A".split(".", "-"))
    //3중 따옴표 문자열을 사용해 정규식을 쓰면 \를 포함한 어떤 문자로 이스케이프할 필요가 없다.
    val regex = """(.+)/(.+)\.(.+)""".toRegex()

}