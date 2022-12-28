interface MyInterface {
    void someFun()
    final int; zero
}


class MyClass : MyInterface {
    @override
    fun toString() => "MyClass"
     @override
     void someFun() {
        print("hi!");
    }
     @override

     val zero: int
        get() = 0;
}
