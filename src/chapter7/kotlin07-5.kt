package chapter7

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

//프로퍼티 접근자 로직 재활용: 위임 프로퍼티
//by lazy()를 사용한 프로퍼티 초기화 지연
//지연 초기화는 객체의 일부분을 초기화하지 않고, 남겨뒀다가 실제로 그 부분의 값이 필요할 경우 초기화할 때 쓰이는 패턴이다.
data class Email(val email: String)

fun loadEmail(person:Person):List<Email>{
    println("${person.name}의 이메일을 가져옴")
    return listOf(/*...*/)
}

//class Person(val name:String){
//    private var _emails: List<Email>? = null //뒷받침하는 프로퍼티 기법
//    val emails: List<Email>
//        get() {
//            if (_emails == null)
//                _emails = loadEmail(this) //최초 접근 시 이메일을 가져온다.
//            return _emails!! //저장해둔 데이터가 있으면 그 데이터를 반환
//        }
//}

//위임 프로퍼티는 데이터를 저장할 때 쓰이는 뒷받침하는 프로퍼티와 값이 오직 한 번만 초기화됨을 보장하는 게터 로직을 함께 캡슐화해준다.
class Person(val name:String){
    val emails by lazy { loadEmail(this) } //위임 프로퍼티를 통해 지연 초기화 구현
}

//위임 프로퍼티 구현
//어떤 객체의 프로퍼티가 바뀔 때마다 리스너에게 변경 통지를 보내는 기능을 구현

//위임 프로퍼티 없이 구현
//PropertyChangeSupport를 사용하기 위한 도우미 클래스
open class PropertyChangeAware{
    protected val changeSupport = PropertyChangeSupport(this)
    fun addPropertyChangeListener(listener: PropertyChangeListener){
        changeSupport.addPropertyChangeListener(listener)
    }
    fun removePropertyChangeListener(listener: PropertyChangeListener){
        changeSupport.removePropertyChangeListener(listener)
    }
}

class Person2(val name:String, age:Int, salary:Int): PropertyChangeAware(){
    var age:Int = age
        set(newValue) {
            val oldValue = field //뒷받침하는 필드에 접근 시 field 식별자 사용
            field = newValue
            changeSupport.firePropertyChange("age", oldValue, newValue) //프로퍼티 변경을 리스너에게 통지
        }

    var salary:Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange("salary", oldValue, newValue)
        }

}


fun main(){
    val p = Person("Alice")
    p.emails //최초로 emails를 읽을 때 단 한번만 이메일을 가져온다.
    p.emails

    var person = Person2("Dmitry", 34, 2000)
    person.addPropertyChangeListener(
        PropertyChangeListener { event ->
            println("Property ${event.propertyName} changed from ${event.oldValue} to ${event.newValue}")
        }
    )
    person.age = 35
    person.salary = 3000

}