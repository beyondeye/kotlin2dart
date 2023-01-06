package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.ForLoopIndicesRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class ForLoopIndicesRuleTest {
    @Test
    fun `for loop converted to dart syntax`() {
        val code =
            """
            fun main()
            {
                for(i in 0 .. 3) println(i)

                val i=-1
                println(i) //-1
                for(i in 0..3) { // 0 1 2 3
                    println(i)
                }
                println(i) //-1

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
                    println("the element at ${"$"}index is ${"$"}value")
                }
            }
            """.trimIndent()
        val formattedCode =
            """
            fun main()
            {
                for ( var i=0; i<=3; i++ ) println(i)

                val i=-1
                println(i) //-1
                for ( var i=0; i<=3; i++ ) { // 0 1 2 3
                    println(i)
                }
                println(i) //-1

                for ( var i=0; i<3; i++ ) { // 0 1 2
                    println(i)
                }

                for ( var i=3; i>=1; i-- ) { // 3 2 1
                    println(i)
                }

                for ( var i=6; i>=1; i-=2 ) { //6 4 2
                    println(i)
                }

                for ( var i=0; i<=3; i+=2 ) { // 0 2
                    println(i)
                }

                val array = listOf(0,11,22,33)
                for (i in array.indices) { //0 11 22 33
                    println(array[i])
                }

                for ((index, value) in array.withIndex()) { //0 11 22 33
                    println("the element at ${"$"}index is ${"$"}value")
                }
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(ForLoopIndicesRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
