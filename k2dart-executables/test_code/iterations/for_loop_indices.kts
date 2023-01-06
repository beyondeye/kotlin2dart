fun main()
{
    for(i in 0 .. 3) println(i)

    val i=-1
    println(i) //-1
    for(i in 0..3) { // 0 1 2 3
        println(i)
    }

    val loop_start=0
    val loop_end=3
    for(i in loop_start..loop_end) { // 0 1 2 3
        println(i)
    }

    for(i in loop_start+1..loop_end+2) { // 0 1 2 3
        println(i)
    }

    for(i in (2*loop_start+1)..(2*loop_end+2)) { // 0 1 2 3
        println(i)
    }

    for(i:Int in 0 until 3) { // 0 1 2
        println(i)
    }

    for(i:Int in 3 downTo 1) { // 3 2 1
        println(i)
    }

    for(i:Int in 6 downTo 1 step 2) { //6 4 2
        println(i)
    }

    for(i in 0..3 step 2) { // 0 2
        println(i)
    }

    val array = listOf(0,11,22,33)
    for (i in array.indices) { //0 11 22 33
        println(array[i])
    }

    for ((index, value) in array.withIndex()) { //0 11 22 33
        println("the element at $index is $value")
    }
}
