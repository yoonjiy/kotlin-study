data class postPhoneComputation(val delay:Int, val computation:Runnable)

fun main(){
    //5.3. 지연 계산 컬렉션 연산
    //시퀀스를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 수행할 수 있다.
    //컬렉션 함수를 연쇄하여 매 단계 중간 결과를 새로운 컬렉션에 임시로 담는다면 원소가 많아질 경우 효율이 떨어진다. -> 각 연산이 컬렉션을 직접 사용하는 대신 시퀀스를 사용하게 만든다.
    val people = listOf(Person2("bob", 20), Person2("alice", 22), Person2("joy", 22), Person2("joyce", 28))
    people.asSequence()  //원본 컬렉션을 시퀀스로 변환
        .map(Person2::name)
        .filter{ it.startsWith("j")}
        .toList() //결과 시퀀스를 다시 리스트로 변환. 시퀀스 인터페이스는 iterator 메소드밖에 없음. -> 인덱스를 이용해 접근하는 등 다른 api가 필요하면 list로 변환 후 사용.

    //시퀀스 연산 실행: 중간 연산과 최종 연산
    //중간 연산은 항상 지연 계산된다.
    listOf(1,2,3,4).asSequence()
        .map{print("map$it "); it*it}
        .filter{print("filter$it "); it%2==0}
    //위 코드는 아무것도 출력되지 않는다. 이는 map과 filter의 변환이 지연돼서 결과를 얻을 필요가 있을 때(즉 최종 연산이 호출될 때) 적용되기 때문이다.

    listOf(1,2,3,4).asSequence()
        .map{ print("map($it) "); it*it}
        .filter{ print("filter($it) "); it%2==0}
        .toList()
    //시퀀스의 경우 모든 연산은 각 원소에 대해 순차적으로 적용된다.
    //따라서 원소에 연산을 차례대로 적용하다가 결과가 얻어지면 그 이후의 원소에 대해서는 변환이 이뤄지지 않을 수도 있다.
    println(listOf(1,2,3,4).asSequence().map{ it*it }.find{ it>3 })
    //시퀀스를 사용하면 지연 계산으로 인해 원소 중 일부의 계산이 이뤄지지않는다.

    //시퀀스 만들기
    val naturalNumbers = generateSequence(0) { it+1 }
    val numbersTo100 = naturalNumbers.takeWhile { it<=100 }
    println(numbersTo100.sum()) //모든 지연 연산은 sum의 결과를 계산할 때 수행된다.

    //5.4. 자바 함수형 인터페이스 활용
    //추상 메소드가 단 하나만 있는 인터페이스 - 함수형 인터페이스 또는 SAM 인터페이스라고 한다.
    //코틀린은 함수형 인터페이스를 인자로 취하는 자바 메소드를 호출할 때 람다를 넘길 수 있게 해준다.
    postPhoneComputation(1000){println(42)} //프로그램 전체에서 runnable 인스턴스는 단 하나만 만들어짐.
    postPhoneComputation(1000, object:Runnable{ //명시적 객체 식을 함수형 인터페이스 구현으로 넘긴다.
        override fun run() { //메소드 호출할 때마다 새로운 객체가 생성됨.
            println(42)
        }
    })

    val runnable = Runnable{println(42)} //Runnable 인스턴스를 변수에 저장
    fun handleComputation(){
        postPhoneComputation(1000, runnable) //모든 호출에 같은 객체를 사용.
    }

    //람다가 주변 영역에 변수를 포획한다면 매번 새로운 인스턴스를 생성해줌.
    fun handleComputation2(id:Int){
        postPhoneComputation(1000){println(id)} //변수 포획.
    }

    //SAM 생성자: 람다를 함수형 인터페이스로 명시적으로 변경
    fun createAllDoneRunnable():Runnable{
        return Runnable{println("ALL DONE!")} //sam 생성자는 그 함수형 인터페이스의 유일한 추상 메소드의 본문에 사용할 람다만을 인자로 받아서 함수형 인터페이스를구현하는 클래스의 인스턴스를 반환.
    }
    createAllDoneRunnable().run()

    //수신 객체 지정 람다: with와 apply
    //with 함수. 파라미터가 2개인 함수로서 첫번째 인자로 받은 객체를 두번째 인자로 받은 람다의 수신 객체로 만든다.
    fun alphabet() = with(StringBuilder()) { //메소드를 호출하려는 수신 객체를 지정한다.
        for (letter in 'A'..'Z') {
            this.append(letter) //this를 명시해서 앞에서지정한 수신 객체의 메소드를 호출한다.
        }
        append("\nNow I know the alphabet!") //this를 생략하고 메소드를 호출한다.
        this.toString() //람다에서 값을 반환한다.
    }
    println(alphabet())

    //apply 함수. 항상 자신에게 전달된 객체를 반환.
    fun alphabet2() = StringBuilder().apply{ //확장 함수로 정의.
        for(letter in 'A'..'Z'){
            append(letter)
        }
        append("\nNow I know the alphabet~")
    }.toString() //apply를 실행한 결과는 StringBuilder 객체.

    fun alphabet3() = buildString{ //StringBuilder 객체 만드는 일과 toString()을 호출하는 일을 알아서 해줌.
        for(letter in 'A'..'Z'){
            append(letter) //항상 수신 객체는 StringBuilder임.
        }
    }



}