package chapter7

import java.math.BigDecimal

//어떤 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법을 코틀린에서는 관례라고 부른다. 언어 기능을 타입에 의존하는 자바와 달리 코틀린은 관례에 의존한다.
//이항 산술 연산 오버로딩
data class Point(val x:Int, val y:Int){
    operator fun plus(other:Point):Point{
        return Point(x+other.x, y+other.y)
    }
}

//연산자를 멤버 함수로 만드는 대신 확장 함수로 정의
operator fun Point.minus(other:Point):Point{
    return Point(x-other.x, y-other.y)
}

//두 피연산자의 타입이 다른 연산자 정의
operator fun Point.times(scale:Double):Point{
    return Point((x*scale).toInt(), (y*scale).toInt())
}
//코틀린 연산자가 자동으로 교환 법칙을 지원하지는 않음에 유의. 1.5 * p는 Double.times()를 따로 정의해야 함.

//결과 타입이 피연산자 타입과 다른 연산자 정의
operator fun Char.times(count:Int):String{
    return toString().repeat(count)
}

//복합 대입 연산자 오버로딩
operator fun <T> MutableCollection<T>.plusAssign(element:T){
    this.add(element)
}

//단항 연산자 오버로딩
operator fun Point.unaryMinus():Point{
    return Point(-x, -y)
}

operator fun BigDecimal.inc() = this + BigDecimal.ONE


fun main(){
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2) //p1.plus(p2)로 컴파일

    val p = Point(10, 20)
    println(p*1.5) //p.times(1.5)로 컴파일

    println('a'*3)

    var point = Point(1, 2)
    point += Point(3,4)
    println(point)

    //+= 연산이 객체에 대한 참조를 다른 참조로 바꾸기보다 원래 객체의 내부 상태를 변경하게 만들고 싶을 때
    val numbers = ArrayList<Int>()
    numbers += 42
    println(numbers[0])

    //+와 -는 항상 새로운 컬렉션을 반환하고, +=, -= 연산자는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킨다.
    // -=, +=가 읽기 전용 컬렉션에 적용될 경우 변경을 적용한 복사본을 반환한다.
    val list = arrayListOf(1,2)
    list += 3 //+=는 list를 변경한다.
    val newList = list + listOf(4,5) //+는 두 리스트의 모든 원소를 포함하는 새로운 리스트를 반환한다.
    println(list)
    println(newList)

    println(-p)

    var bd = BigDecimal.ZERO
    println(bd++) //println이 실행된 다음 값을 증가시킴
    println(++bd) //값을 증가시키고 println 실행
}