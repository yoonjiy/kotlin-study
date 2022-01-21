package chapter4

//4.2. 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언

//클래스 초기화: 주 생성자와 초기화 블록
//초기화 블록은 주 생성자와 함께 사용.
open class User2(val nickname:String){}
class TwitterUser(nickname:String):User2(nickname){}
open class Button4 // 인자가 없는 디폴트 생성자가 만들어진다.
class RadioButton:Button4() //Button 클래스를 상속한 하위 클래스에서는 반드시 Button 클래스의 생성자를 호출해야 한다.
//인터페이스는 생성자가 없기 때문에 인터페이스를 구현한 하위 클래스의 상위 클래스 이름 뒤에는 괄호가 없다.

//어떤 클래스를 클래스 외부에서 인스턴스화 하지 못하게 막고 싶다면 모든 생성자를 private으로 만든다.
class Secretive private constructor(){}

//부 생성자(constructor로 시작). 부 생성자를 여럿 만들지 않고 파라미터의 디폴트 값을 생성자 시그니처에 직접 명시하는 편이 좋다.

//코틀린에서는 인터페이스에 추상 프로퍼티 선언을 넣을 수 있다.
//인터페이스의 프로퍼티 구현하기
interface UserInterface{
    val nickname:String //추상 프로퍼티
}
class PrivateUser(override val nickname:String):UserInterface //주 생성자에 있는 프로퍼티
class SubscribingUser(val email:String):UserInterface{
    override val nickname:String
        get()= email.substringBefore('@') //커스텀 게터. 뒷받침하는 필드에 값을 저장하지 않고 매번 계산 후 반환.

}
//class FaceBookUser(val accountId:Int):UserInterface{
//    override val nickname = getFaceBookName(accountId) //프로퍼티 초기화 식. 객체를 초기화하는 단계에 한번만 호출. 뒷받침하는 필드에 저장했다가 불러오는 방식.
//}

//게터와 세터가 있는 프로퍼티 역시 인터페이스에 선언 가능.(인터페이스는 상태를 저장할 수 없다->인터페이스에 선언된 게터와 세터는 뒷받침하는 필드를 참조할 수 없다.)
interface UserInterface2{
    val email:String //추상 프로퍼티. 반드시 오버라이드해야 함.
    val nickname:String //하위클래스에서 오버라이드하지 않고 상속 가능.
        get() = email.substringBefore('@') //커스텀 게터. 프로퍼티에 뒷받침하는 필드가 없다.
}

//게터와 세터에서 뒷받침하는 필드에 접근
//세터에서 뒷받침하는 필드 접근하기.
class User3(val name:String){
    var address:String = "unspecified"
        set(value:String){
            println("""
                Address was changed for $name:
                "$field" -> "$value".""".trimIndent()) //뒷받침하는 필드 값 읽기. 접근자의 본문에서는 field라는 식별자를 이용해 뒷받침하는 필드에 접근할 수 있다.
            field = value //뒷받침하는 필드 값 변경하기
        }
}

//접근자의 가시성 변경. 보통 접근자의 가시성은 프로퍼티의 가시성과 같다.
class LengthCounter{
    var counter:Int = 0
        private set //이 클래스 밖에서 이 프로퍼티의 값을 바꿀 수 없다.
    fun addWord(word:String){
        counter += word.length
    }
}

//4.3. 데이터 클래스와 클래스 위임
//어떤 클래스가 데이터를 저장하는 역할만을 수행한다면 toString, equals, hashCode를 반드시 오버라이딩해야 한다. 코틀린의 데이터 클래스는 필요한 메소드를 컴파일러가 자동으로 만들어준다.
data class Client(val name:String, val postalCode:Int) //모든 프로퍼티를 읽기 전용(val)-> 불변 클래스로 만들기를 권장.

//copy() 메소드. 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해줌.
class Client2(val name:String, val postalCode: Int){
    override fun toString(): String = "Client(name=$name, postalcode=$postalCode)"
    fun copy(name:String=this.name, postalCode:Int=this.postalCode) = Client2(name, postalCode)
}

//클래스 위임: by 키워드.
//인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스에 대한 구현을 다른 객체에 위임 중이라는 사실을 명시 가능.
class CountingSet<T>(
    val innerSet: MutableCollection<T> = HashSet<T>()
): MutableCollection<T> by innerSet{ //MutableCollection의 구현을 innerSet에게 위임한다.
    var objectsAdded = 0
    override fun add(element:T):Boolean{ //이 메소드는 위임하지 않고 새로운 구현을 제공
        objectsAdded++
        return innerSet.add(element)
    }
    override fun addAll(c:Collection<T>):Boolean { //이 메소드는 위임하지 않고 새로운 구현을 제공
        objectsAdded += c.size
        return innerSet.addAll(c) //CountingSet에 MutableCollection의 구현 방식에 대한 의존관계가 생기지 않는다.
    }
}

fun main(){
    val user = User3("Alice")
    user.address = "Ewha 52 street" //세터

    var lengthCounter = LengthCounter()
    lengthCounter.addWord("hi!")
    println(lengthCounter.counter)
    //lengthCounter.counter = 5. private setter.

    val client1 = Client("Bob", 12)
    println(client1.toString())
    println(lengthCounter.toString())

    val lee = Client2("이사람", 4122)
    println(lee.copy(postalCode = 4000))

    val cset = CountingSet<Int>()
    cset.addAll(listOf(1,2,3))
    println("${cset.objectsAdded} objects were added, ${cset.size} remain")

}