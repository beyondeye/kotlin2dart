class A
{
}


/* suspend */ Future<A> fun1()
{
    return A();
}

/* suspend */ Future<Object>  fun2()=>12

/* suspend */ Future<int> anotherAsyncFun() {
    return 1;
}

/* suspend */ Future<List<A>> yetAnotherAsyncFun() {
    return 1;
}
