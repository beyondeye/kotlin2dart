class SomeClass

data class A(val a:Int, val b:String, var c:SomeClass)


data class B(val c:String, var d:Double) {
    fun someMethod() { print("Hi") }
}
