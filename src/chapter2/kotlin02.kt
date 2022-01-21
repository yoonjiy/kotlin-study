package chapter2

//2.1. 함수와 변수
import java.io.BufferedReader
import java.io.StringReader
import java.lang.NumberFormatException
import java.lang.module.ModuleDescriptor
import java.util.*

fun max(a:Int, b:Int) = if (a>b) a else b //식이 본문인 함수. -> 이 경우의 반환 타입만 생략이 가능. 타입 추론.

//val = value 값. var = variable 변수
fun second(){
    val a:Int = 10 //immutable. 자바의 final 변수. 기본적으로 모든 변수를 불변 변수로 선언하는 것이 좋음. -> 함수형 코드
    var b:Int = 9 //mutable. 자바의 일반 변수
    b = 100
    val c = "yoonji"
    var e:String //초기화하지 않고 선언만 한다면 반드시 타입을 명시 해야 함.

    val language = arrayListOf("java", "c++")
    language.add("kotlin") //참조가 가리키는 객체 내부 변경 가능. 참조는 동일.
}

//2.1. 문자열 템플릿
fun StringTemplate(array:Array<String>){
    val name = if(array.size>0) array[0] else "Kotlin"
    println("hello, $name")
}

//2.2. 클래스와 프로퍼티
class Rectangle(val height: Int, val width:Int){
    val isSquare:Boolean
        get() = height == width //프로퍼티 게터 선언
}
fun createRandomRectangle(): Rectangle{
    val random = Random()
    return Rectangle(random.nextInt(), random.nextInt())
}

//2.3. enum과 when
//프로퍼티와 메소드가 있는 enum 클래스 정의
enum class Color(
    val r:Int, val g:Int, val b:Int
){
    RED(255, 0, 0), ORANGE(255, 165, 0), YELLOW(255,0,0), GREEN(0, 255, 0), BLUE(0,0,255), INGIDO(75, 0, 130), VIOLET(238, 130, 238);
    fun rgb() = (r * 256 + g) * 256 + b
}

//when으로 올바른 enum 값 찾기
fun getMnemonic(color:Color) =
    when(color){
        Color.RED -> "Richard"
        Color.ORANGE -> "of"
        Color.YELLOW -> "York"
        Color.GREEN -> "Gave"
        Color.BLUE -> "Battle"
        Color.INGIDO -> "In"
        Color.VIOLET -> "Vain"
    }

//한 when 분기 안에 여러 값 사용하기
fun getWarmth(color: Color) = when(color){
    Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
    Color.GREEN -> "neutral"
    else -> "cold"
}

//when의 분기 조건에 여러 다른 객체 사용하기
fun mix(c1: Color, c2:Color) =
    when(setOf(c1, c2)){
        setOf(Color.RED, Color.YELLOW) -> Color.ORANGE
        setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
        else ->throw Exception("Dirty color")
    }

//인자가 없는 when 사용 가능. 각 분기의 조건이 불리언 결과를 계산하는 식이어야 한다. 추가 객체를 만들지 않는다는 장점이 있으나 가독성이 떨어진다.
fun mixOptimized(c1:Color, c2:Color) =
    when{
        (c1==Color.RED && c2==Color.YELLOW) || (c1==Color.YELLOW && c2==Color.RED) -> Color.ORANGE
        else -> throw Exception("Dirty color")
    }

//스마트 캐스트: 타입 검사와 타입 캐스트를 조합
interface Expr
class Num(val value:Int): Expr //expr 인터페이스 상속
class Sum(val left:Expr, val right:Expr): Expr

fun eval(e: Expr): Int =
    if (e is Num){ //is를 이용해 변수 타입 검사.
        //val n = e as Num //불필요한 중복 타입 변환
        //return n.value
        e.value
    }
    else if(e is Sum){
        eval(e.right) + eval(e.left) //변수 e에 대해 스마트 캐스트를 사용.
    }
    else {
        throw IllegalArgumentException("Unknown expression")
    }
//타입 검사를 하고 나면 굳이 변수를 원하는 타입으로 캐스팅하지 않아도 마치 처음부터 그 변수가 원하는 타입으로 선언된 것처럼 사용 가능.

//if 중첩 대신 when 사용하기
fun eval2(e:Expr): Int =
    when(e){
        is Num -> e.value
        is Sum -> eval(e.left) + eval(e.right)
        else -> throw IllegalArgumentException("Unknown expression")
    }

//2.4. while과 for 루프
//when을 이용해 피즈버즈 게임 구현
fun fizzBuzz(i: Int) = when{
    i % 15 == 0 -> "FizzBuzz"
    i % 3 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "${i}"
}

//2.5. 예외처리
// try를 식으로 사용하기
fun readNumber(reader: BufferedReader){
    val number = try{
        Integer.parseInt(reader.readLine())
    }catch(e : NumberFormatException){
        null
    }
    println(number)
}

fun main(){
    val array = arrayOf("Bob")
    StringTemplate(array)
    println(Color.BLUE.rgb())
    println(getMnemonic(Color.BLUE))
    println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))

    for (i in 1..100){
        print(fizzBuzz(i))
    }

    for(i in 100 downTo 1 step 2){
        print(fizzBuzz(i))
    }

    //맵에 대한 이터레이션
    val binaryReps = TreeMap<Char, String>()
    for(c in 'A'..'F'){
        val binary = Integer.toBinaryString(c.code)
        binaryReps[c] = binary
    }
    for((letter, binary) in binaryReps){
        println("$letter = $binary")
    }

    //in을 이용해 값이 범위에 속하는지 알아보기
    fun isLetter(c:Char)= c in 'a'..'f' || c in 'A'..'Z'

    val reader = BufferedReader(StringReader("not a number"))
    readNumber(reader) //null 출력.
}
