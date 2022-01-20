import java.io.Serializable

//4.1.클래스 계층 정의
//코틀린 인터페이스. 코틀린 인터페이스 안에서는 추상 메소드 뿐 아니라 구현이 있는 메소드도 정의할 수 있다.
interface Clickable{
    fun click() // 일반 메소드 선언
    fun showOff() = println("I]m clickable!")// 디폴트 구현이 있는 메소드.
}
interface Focusable{
    fun setFocus(b:Boolean)=println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}
/* 한 클래스에서 위의 두 인터페이스를 함께 구현하면 어느쪽 showOff 메소드도 선택되지 않는다.
이 경우 오버라이딩 메소드를 제공하지 않으면 컴파일러 오류가 발생한다.*/

class Button2:Clickable, Focusable{ //자바와 마찬 가지로 인터페이스의 다중 상속 가능. 클래스는 하나만 상속 가능.
    override fun click() = println("I was clicked")
    override fun showOff(){
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
}

/*
open, final, abstract 변경자. 기본적으로 final.
취약한 기반 클래스. 코틀린의 클래스와 메소드는 기본적으로 final. 상속을 금지한다.
상속을 허용하려면 클래스 앞에 open을 붙여야 한다. 오버라이드를 허용하고 싶은 메소드나 프로퍼티의 앞에도 open 변경자를 붙여야 한다.
인터페이스 멤버는 항상 열려있으며 final로 변경할 수 없다.
*/
open class RichButton: Clickable{ //다른 클래스가 이 클래스를 상속할 수 있다.
    fun disable(){} //기본 final. 하위 클래스가 이 메소드를 오버라이드 할 수 없다.
    open fun animate(){} //open. 하위 클래스에서 오버라이드 가능하다.
    override fun click() {} //오버라이드한 메소드는 기본적으로 열려있다.
    //final override fun click(){} <- 오버라이드 금지

}

//추상 클래스 정의하기. abstract로 선언한 추상 클래스는 인스턴스화 할 수 없다.
// 구현이 없는 추상 멤버는 하위 클래스에서 오버라이드 해야 한다. 따라서 추상 멤버는 항상 열려 있으므로 open을 쓸 필요가 없다.
abstract class Animated{
    abstract fun animate() //추상 멤버. 반드시 하위클래스에서 오버라이드 해야 함.
    open fun stopAnimating(){}
    fun animateTwice(){}
}

//가시성 변경자. 아무 변경자도 없는 경우 선언은 모두 public.
// internal - 같은 모듈 안에서만 볼 수 있음 / private - 클래스 멤버인 경우 같은 클래스 안, 최상위 선언인 경우 같은 파일 안.
// protected - 클래스 멤버인 경우 하위 클래스 안, 최상위 선언 적용 불가 / public - 모든 곳에서 볼 수 있음.
internal open class TalkativeButton:Focusable{
    private fun yell()= println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

// public 함수인 giveSpeech 안에서 그보다 가시성이 더 낮은 타입인 TalkativeButton 참조 불가능.
//fun TalkativeButton.giveSpeech(){
//    yell()
//    whisper() 클래스를 확장한 함수는 그 클래스의 private이나 protected 멤버에 접근 불가.
//}


// 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스
// 코틀린의 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다.
interface State:Serializable
interface View2{
    fun getCurrentState():State
    fun restoreState(state:State){}
}

//자바에서 다른 클래스 안에 정의한 클래스는 자동적으로 내부 클래스. 바깥쪽 클래스에 대한 참조를 묵시적으로 포함한다. static으로 선언해야 바깥쪽 클래스에 대한 묵시적인 참조가 사라진다.
class Button3:View2{
    override fun getCurrentState(): State = ButtonState()
    override fun restoreState(state: State) {
        super.restoreState(state)
    }
    class ButtonState: State{} //자바의 static 중첩 클래스와 대응. 바깥쪽 클래스에 대한 참조를 포함하는 내부 클래스로 변경하고 싶다면 inner 변경자를 붙인다.
}

class Outer{
    inner class Inner{
        fun getOuterReference():Outer = this@Outer //바깥쪽 클래스 참조에 접근.
    }
}

// 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한
// 디폴트 분기가 있으면 클래스 계층에 새로운 하위 클래스를 추가하더라도 컴파일러가 when이 모든 경우를 처리하는지 제대로 검사할 수 없다.
// 상위 클래스에 sealed 변경자를 붙이면 그 상위클래스에 속한 하위 클래스 정의를 제한할 수 있다. 이때 하위 클래스는 반드시 상위 클래스 안에 중첩시켜야 한다.
sealed class Expression{  //자동으로 open. 클래스 외부에 자신을 상속한 클래스를 둘 수 없다.
    class Num(val value:Int): Expression() //하위클래스를 중첩클래스로 나열
    class Sum(val left:Expression,val right:Expression): Expression() //기반 클래스 이름 뒤 생성자를 호출 하기 위한 ()
}
fun eval(e:Expression):Int =
    when(e){
        is Expression.Num -> e.value
        is Expression.Sum -> eval(e.right) +eval(e.left) //when이 모든 하위 클래스를 검사하므로 별도의 else 분기가 필요 없다.
    }

fun main(){
    val button = Button2()
    button.showOff()
    button.setFocus(true)
    button.click()

}