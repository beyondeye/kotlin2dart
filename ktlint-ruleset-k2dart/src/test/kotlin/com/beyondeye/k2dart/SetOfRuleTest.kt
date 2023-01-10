package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.SetOfRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SetOfRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            class Car(val producer:String, val model:String)
            fun main() {
                val a = setOf("hello","world","!")
                val b:Set<Int> = setOf(1,2,3,4)
                val c= setOf<Int>(3,2,1)
                val d = setOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
                val a1 = mutableSetOf("hello","world","!")
                val b1:MutableSet<Int> = mutableSetOf(1,2,3,4)
                val c1= mutableSetOf<Int>(3,2,1)
                val d1 = mutableSetOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
            }
            """.trimIndent()
        val formattedCode =
            """
            class Car(val producer:String, val model:String)
            fun main() {
                val a = setOf("hello","world","!")
                val b:Set<Int> = setOf(1,2,3,4)
                val c= setOf<Int>(3,2,1)
                val d = setOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
                val a1 = mutableSetOf("hello","world","!")
                val b1:MutableSet<Int> = mutableSetOf(1,2,3,4)
                val c1= mutableSetOf<Int>(3,2,1)
                val d1 = mutableSetOf<Car>(Car("fiat","500"),Car("toyota","corolla"),Car("ford","fiesta"))
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(SetOfRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
