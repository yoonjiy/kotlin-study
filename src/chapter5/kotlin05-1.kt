package chapter5//5.1. 람다 식과 멤버 참조
/*
클래스를 선언하고 그 클래스의 인스턴스를 함수에 넘기는 대신, 함수형 언어에서는 함수를 직접 다른 함수에 전달할 수 있다.
람다식을 사용하면 함수를 선언할 필요 없이 코드 블록을 직접 함수의 인자로 전달할 수 있다.
*/

//람다와 컬렉션
data class Person2(val name:String, val age:Int)

//람다 식의 문법
val sum = {x:Int, y:Int -> x+y} //람다 식을 변수에 저장할 수 있다. 이 경우 컴파일러가 추론할 문맥이 없으므로 타입을 명시해야 함.

//변수 포획
//람다를 함수 안에서 선언하면 함수의 파라미터와 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 사용할 수 있다.
//코틀린 람다 안에서는 파이널 변수가 아닌 변수에 접근할 수 있다. 람다 안에서 바깥의 변수를 변경해도 된다.
fun printProblemCounts(responses:Collection<String>){
    var clientErrors = 0
    var serverErrors = 0 //람다 안에서 사용하는 외부 변수. -> 람다가 포획한 변수
    responses.forEach(){
        if(it.startsWith("4")){
            clientErrors++
        }
        else if(it.startsWith("5")){ //람다 안에서 람다 밖의 변수를 변경한다.
            serverErrors++
        }
    }

    println("$clientErrors client errors, $serverErrors server errors")
}

//멤버 참조. 코틀린에서는 함수를 값으로 바꿀 수 있다. ::를 사용하는 식을 멤버 참조라고 부른다.
//멤버 참조는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어 준다.
fun salute() = println("Salute!")

fun Person2.isAdult() = this.age >= 21
val predicate = Person2::isAdult //확장 함수도 똑같은 방식으로 멤버 참조 가능.

fun main(){
    val people = listOf(Person2("bob", 20), Person2("alice", 22))
    println(people.maxByOrNull { it.age }) //람다로 컬렉션 검색. maxByOrNull은 비교에 사용할 값을 돌려주는 함수를 인자로 받는다.
    //위와 같이 단순히 함수나 프로퍼티를 반환하는 역할인 람다는 멤버 참조로 대치할 수 있다.
    people.maxByOrNull(Person2::age) //멤버 참조로 컬렉션 검색.

    //람다식을 maxBy 함수의 인자로 넘김. 람다 식은 Person2 타입의 값을 인자로 받아서 age를 반환하는 함수.
    people.maxByOrNull({ p:Person2 -> p.age })

    /*
    - 인자가 하나뿐인 경우 이름을 붙이지 않아도 됨. 컴파일러가 유추 가능한 타입을 적을 필요 없음.
    - 맨 뒤에 있는 인자가 람다식이라면 람다를 괄호 밖으로 빼낼 수 있음.
    - 람다의 파라미터가 하나 뿐이고 그 타입을 컴파일러가 추론할 수 있다면 it을 바로 쓸 수 있음.
    - 본문이 여러 줄로 이뤄진 겨우 본문의 맨 마지막에 있는 식이 람다의 결과값이 됨.
    */
    people.joinToString(" "){ it.name }

    println(sum(1,2))
    run{println(42)} //코드의 일부분을 블록으로 둘러싸 실행할 필요가 있다면 run(인자로 받은 람다를 실행)을 사용한다.

    val responses = listOf("200 ok", "418 error", "500 internal server error", "404 not found", "400 bad request")
    printProblemCounts(responses)

    run(::salute) // 최상위 함수를 참조한다.
    //val action = {person:Person, message:String-> sendEmail(person, message)}  람다가 sendEmail 함수에게 작업을 위임
    //val nextAction = ::sendEmail 람다대신 멤버참조 사용.

    val createPerson2 = ::Person2 //생성자 참조. Person2 인스턴스를 만드는 동작을 값으로 저장.
    val p = createPerson2("joy", 25)
    println(p)

}