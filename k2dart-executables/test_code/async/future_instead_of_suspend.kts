class A

suspend fun fun1():A
{
    return A()
}

suspend fun fun2()=12

suspend fun anotherAsyncFun():Int {
    return 1
}

suspend fun yetAnotherAsyncFun():List<A> {
    return 1
}
