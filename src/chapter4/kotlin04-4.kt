package chapter4

import java.io.File

//4.4 object 키워드
/*
- 객체 선언은 싱글턴을 정의하는 방법 중 하나이다.
- 동반 객체는 인스턴스 메소드는 아니지만 어떤 클래스와 관련있는 메소드와 팩토리 메소드를 담을 때 쓰인다.
  동반 객체 메소드에 접근할 때는 동반 객체가 포함된 클래스의 이름을 사용할 수 있다.
- 객체 식은 자바의 무명 내부 클래스 대신 쓰인다.
*/

//생성자는 객체 선언에 쓸 수 없다. 싱클턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없이 즉시 만들어 진다.
//객체 선언도 클래스나 인터페이스를 상속할 수 있다. -> 특정 인터페이스를 구현해야 하는데, 그 구현 내부에 다른 상태가 필요하지 않은 경우.
object CaseInsensitiveFileComparator: Comparator<File>{ //Comparator 안에는 데이터를 저장할 필요가 없다.
    override fun compare(file1: File, file2: File): Int {
        return file1.path.compareTo(file2.path, ignoreCase = true)
    }
}

//중첩 객체를 사용해 Comparator 구현하기
data class Person(val name:String){
    object NameComparator:Comparator<Person>{
        override fun compare(p1:Person, p2:Person):Int = p1.name.compareTo(p2.name)
    }
}

//동반 객체. 동반 객체의 프로퍼티나 메소드에 접근하려면 그 동반객체가 정의된 클래스의 이름을 사용. -> like 자바의 정적 메소드 호출, 정적 필드 사용 구문
//자바의 static 키워드 -> 코틀린의 최상위 함수(정적 메소드 역할을 거의 대신함.) + 객체 선언.
//하지만 최상위 함수는 private으로 표시된 클래스 비공개 멤버에 접근할 수 없다. -> 이 경우 클래스에 중첩된 객체 선언의 멤버 함수로 정의해야 함.

//동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근할 수 있다. - 팩토리 패턴을 구현하기 가장 적합함.

class User4{
    val nickname:String
    constructor(email:String){
        nickname = email.substringBefore('@')
    }
//  constructor(facebookAccountId:Int){
//      nickname = getFaceBookName(facebookAccountId)
//  }
}

//부생성자를 팩토리 메소드로 대신하기. but 클래스를 확장해야만 하는 경우에 동반 객체 멤버를 하위클래스에서 오버라이드 할 수 없다.
class User5 private constructor(val nickname:String){ //주 생성자 - 비공개
    companion object{
        fun newSubscribingUser(email:String) = User5(email.substringBefore('@'))
        //fun newFacebookUser(accountId:Int) = User5(getFaceBookName(accountId))
    }
}

//동반 객체는 일반 객체처럼 이름을 붙이거나 인터페이스를 구현하게 할 수 있다.
//클래스에 동반 객체가 있으면(빈 객체라도) 클래스 밖에서 동반객체에 대한 확장 함수를 작성할 수 있다.

//객체 식: 무명 내부 클래스를 다른 방식으로 작성.
//보통 함수를 호출하면서 인자로 무명 객체를 넘기기 때문에 클래스와 인스턴스 모두 이름이 필요 없음.
//window.addMouseListener(
//    object:MouseAdapter(){    MouseAdapter를 확장하는 무명 객체 선언. 싱글턴이 아님.
//        override fun mouseClicked(e:MouseEvent){
//
//        }
//    }
//)

fun main(){
    println(CaseInsensitiveFileComparator.compare(File("/User"), File("/user")))
    val persons = listOf(Person("Bob"), Person("Alice"))
    println(persons.sortedWith(Person.NameComparator))

    val subscribingUser = User5.newSubscribingUser("bob@email.com")
    println(subscribingUser.nickname)
 }