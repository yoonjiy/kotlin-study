class Book(val title:String, val authors:List<String>)

fun main(){
    //5.2. 컬렉션 함수형 api
    //filter와 map
    val people = listOf(Person2("bob", 20), Person2("alice", 22), Person2("joy", 22))

    val list = listOf(1,2,3,4)
    println(list.filter{ it%2==0 })
    println(people.filter{ it.age>=22 }) //filter 함수는 컬렉션에 원치 않는 원소를 제거한다.하지만 원소를 변환할 수는 없다.

    //map 함수는 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션으로 만든다.
    println(list.map{ it*it })
    println(people.map{ it.name })
    people.filter{ it.age>=22 }.map(Person2::name)

    val maxAge = people.maxByOrNull(Person2::age)!!.age
    people.filter{ it.age == maxAge }

    val numbers = mapOf(0 to "zero", 1 to "one")
    println(numbers.mapValues { it.value.uppercase() })

    //all, any, count,find: 컬렉션의 술어에 적용
    val canBeInClub25 = {p:Person2 -> p.age <= 25}
    println(people.all(canBeInClub25)) //모든 원소가 만족하는지
    println(people.any(canBeInClub25)) //한 원소라도 만족하는지
    println(people.count(canBeInClub25))
    println(people.find(canBeInClub25)) //가장 먼저 조건을 만족한다고 확인된 원소를 반환.

    //groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
    println(people.groupBy{ it.age }) //Map<Int, List<Person>>
    val a = listOf("a", "ab", "b")
    println(a.groupBy(String::first)) //first는 String의 확장함수

    //flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리
    //flatMap 함수는 인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고, 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 모은다.
    val books = listOf(Book("title1", listOf("author1", "author2")), Book("title2", listOf("author2", "author2-2", "author2-3")))
    println(books.flatMap { it.authors }.toSet()) //books 컬렉션에 있는 책을 쓴 모든 저자의 집합
    val strings = listOf("abc", "def")
    println(strings.flatMap { it.toList() })


}