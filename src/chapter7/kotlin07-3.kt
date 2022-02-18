package chapter7

import java.time.LocalDate

//컬렉션과 범위에 대한 관례
//인덱스로 원소 접근: get, set

//get 관례 구현
operator fun Point.get(index: Int): Int{
    return when(index){
        0 -> x
        1 -> y //주어진 인덱스에 해당하는 좌표를 찾는다.
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

//set 관례 구현
data class MutablePoint(var x:Int, var y:Int)
operator fun MutablePoint.set(index:Int, value:Int){
    when(index){
        0 -> x = value //주어진 인덱스에 해당하는 좌표 변경
        1 -> y = value
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

//in 관례 - contains 함수와 대응
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p:Point):Boolean{
    return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y

}

//for 루프를 위한 iterator 관례
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object:Iterator<LocalDate>{
        var current = start
        override fun hasNext() = current <= endInclusive //compareTo 관례를 사용해 날짜를 비교
        override fun next() = current.apply{
            current = plusDays(1) //현재 날짜를 1일 뒤로 변경
        }
    }

fun main(){
    val p1 = Point(10, 20)
    println(p1[1])

    val p2 = MutablePoint(10, 20)
    p2[1] = 42
    println(p2)

    val rect = Rectangle(Point(10, 20), Point(50, 50))
    println(Point(20,30) in rect)

    //rangeTo 관례
    val now = LocalDate.now()
    val vacation = now..now.plusDays(10)
    println(now.plusWeeks(1) in vacation)

    val newYear = LocalDate.ofYearDay(2022, 1)
    val daysOff = newYear.minusDays(1)..newYear
    for(dayOff in daysOff){
        println(dayOff)
    }
}