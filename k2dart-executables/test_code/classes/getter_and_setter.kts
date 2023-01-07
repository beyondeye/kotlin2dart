class A {
    val a:String get() { return "Hi"}
    val b:String get() = "Hello"

    var c:Int=0
        set(value) {
            field=value
        }

    private var _d:Int=0
    var d
        set(value) {
            _d=value
        }
        get() = _d

}

fun main()
{

    val a=A()
    println(a.a)
    println(a.b)
    a.c=1
    println(a.c)

    a.d=3
    println(a.d)

}
