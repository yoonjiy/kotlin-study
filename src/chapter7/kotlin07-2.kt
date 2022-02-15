package chapter7

//비교 연산자 오버로딩

//동등성 연산자 equals를 직접 구현한다면 다음과 비슷한 코드가 됨.
class Point2(val x:Int, val y:Int){
    override fun equals(obj: Any?): Boolean{
        if(obj === this) return true //최적화: 파라미터가 this와 같은 객체인지
        if(obj !is Point) return false
        return obj.x==x && obj.y==y //Point로 스마트 캐스트 후 접근
    }
}

//순서 연산자 compareTo
class Person4(val firstName:String, val lastName:String): Comparable<Person4>{
    override fun compareTo(other: Person4): Int {
        return compareValuesBy(this, other, Person4::lastName, Person4::firstName)
    }
}

fun main(){
    println(Point2(10, 20) == Point2(10, 20))
    println(Point2(10, 20) != Point2(5, 5))
    println(null == Point(1, 2))

    val p1 = Person4("Alice", "Smith")
    val p2 = Person4("Bob", "Johnson")
    println(p1 < p2)
}