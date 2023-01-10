package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.ListOfRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
class ListOfRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            class Car(val producer:String, val model:String)
            fun main() {
                val a = listOf("hello","world","!")
                val b:List<Int> = listOf(1,2,3,4)
                val c= listOf<Int>(3,2,1)
                val d = listOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
                val a1 = mutableListOf("hello","world","!")
                val b1:MutableList<Int> = mutableListOf(1,2,3,4)
                val c1= mutableListOf<Int>(3,2,1)
                val d1 = mutableListOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
            }
            """.trimIndent()
        val formattedCode =
            """
            class Car(val producer:String, val model:String)
            fun main() {
                val a = listOf("hello","world","!")
                val b:List<Int> = listOf(1,2,3,4)
                val c= listOf<Int>(3,2,1)
                val d = listOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
                val a1 = mutableListOf("hello","world","!")
                val b1:MutableList<Int> = mutableListOf(1,2,3,4)
                val c1= mutableListOf<Int>(3,2,1)
                val d1 = mutableListOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(ListOfRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
