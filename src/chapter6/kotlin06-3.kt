package chapter6

import java.io.BufferedReader
import java.io.StringReader

//컬렉션과 배열
//널 가능성과 컬렉션
fun readNumbers(reader: BufferedReader): List<Int?>{
    val result = ArrayList<Int?>() //널이 될 수 있는 Int 값으로 이뤄진 리스트
    for(line in reader.lineSequence()){
        try{
            val number = line.toInt()
            result.add(number)
        }
        catch(e:NumberFormatException){
            result.add(null) //현재 줄을 파싱할 수 없으므로 리스트에 널을 추가.
        }
    }
    return result
}

fun addValidNumbers(numbers:List<Int?>){
    var sumOfValidNumbers = 0
    var invalidNumbers = 0
    for (number in numbers){
        if(number!=null){
            sumOfValidNumbers += number
        }
        else{
            invalidNumbers++
        }
    }
    println("Sum of valid numbers: $sumOfValidNumbers")
    println("Invalid numbers: $invalidNumbers")
}

//filterNotNull 함수 사용
fun addValidNumbers2(numbers:List<Int?>){
    val validNumbers = numbers.filterNotNull() //validNumbers는 컬렉션 내 널이 없음을 보장, List<Int> 타입.
    println("Sum of valid numbers: ${validNumbers.sum()}")
    println("Invalid numbers: ${numbers.size - validNumbers.size}")
}

//읽기 전용과 변경 가능한 컬렉션
//MutableCollection은 Collection을 확장하면서 원소의 add,remove,clear 등의 메소드를 추가적으로 제공. 가능하면 항상 읽기 전용으로..
fun <T> copyElements(source: Collection<T>, target:MutableCollection<T>){
    for(item in source){
        target.add(item) //MutableCollection에 원소 추가.
    }
}

//코틀린 컬렉션과 자바.
//자바는 읽기 전용 컬렉션과 변경 가능 컬렉션을 구분하지 않으므로, 코틀린에서 읽기 전용으로 선언된 객체라도 자바에 넘기면 객체의 내용이 변경될 수 있다. -> 개발자의 책임..

fun main(){
    //널 가능성과 컬렉션
    val reader = BufferedReader(StringReader("1\nabc\n42"))
    val numbers = readNumbers(reader)
    addValidNumbers(numbers)

    //읽기전용과 변경 가능한 컬렉션
    val source:Collection<Int> = arrayListOf(3,5,7)
    val target:MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
    println(target)
}
