void main()
{
    for ( var i=0; i<=3; i++ ) println(i)

    final i=-1;
    println(i); //-1
    for ( var i=0; i<=3; i++ ) { // 0 1 2 3
        println(i);
    }

    final loop_start=0;
    final loop_end=3;
    for ( var i=loop_start; i<=loop_end; i++ ) { // 0 1 2 3
        println(i);
    }

    for ( var i=loop_start+1; i<=loop_end+2; i++ ) {
        println(i);
    }

    for ( var i=(2*loop_start+1); i<=(2*loop_end+2); i++ ) {
        println(i);
    }

    for ( var i=0; i<3; i++ ) { // 0 1 2
        println(i);
    }

    for ( var i=3; i>=1; i-- ) { // 3 2 1
        println(i);
    }

    for ( var i=6; i>=1; i-=2 ) { //6 4 2
        println(i);
    }

    for ( var i=0; i<=3; i+=2 ) { // 0 2
        println(i);
    }

    final array = listOf(0,11,22,33);
    for (i in array.indices) { //0 11 22 33
        println(array[i]);
    }

    for ((index, value) in array.withIndex()) { //0 11 22 33
        println("the element at $index is $value");
    }
}
