interface MyInterface {
    fun someFun()
    val zero:Int
}


class MyClass : MyInterface {
    @Override
    fun toString() = "MyClass"
    override fun someFun() {
        print("hi!")
    }

    override val zero: Int
        get() = 0
}
