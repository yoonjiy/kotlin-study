package chapter7
//구조 분해 선언과 component 함수

//구조 분해 선언은 함수가 여러 값을 한꺼번에 반환할 때 유용하다.
data class NameComponents(val name:String, val extension: String)

fun splitFilename(fullName:String): NameComponents{
    val (name, extension) = fullName.split('.', limit=2)
    return NameComponents(name, extension);
}

//루프 안에서도 구조 분해 선언을 사용할 수 있다.
fun printEntries(map: Map<String, String>){
    for ((key, value) in map){
        println("$key -> $value")
    }
}

fun main(){
    val (name, ext) = splitFilename("example.kt")
    println(name)
    println(ext)

    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
    printEntries(map)
}