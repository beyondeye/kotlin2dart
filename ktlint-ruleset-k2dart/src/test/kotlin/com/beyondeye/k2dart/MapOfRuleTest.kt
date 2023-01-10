package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.MapOfRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MapOfRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            class Car(val producer:String,val model:String)
            fun main() {
                val a = mapOf("hello" to "world", "hi" to "universe")
                val b:Map<Int,Int> = mapOf(1 to 2,3 to 4)
                val c= mapOf<Int,Int>(3 to 2,1 to 0)
                val d = mapOf<String,Car>("fiat500" to Car("fiat","500"),"corolla" to Car("toyota","corolla"),"fiesta" to Car("ford","fiesta"))
                val a1 = mutableMapOf("hello" to "world", "hi" to "universe")
                val b1:MutableMap<Int,Int> = mutableMapOf(1 to 2,3 to 4)
                val c1= mutableMapOf<Int,Int>(3 to 2,1 to 0)
                val d1 = mutableMapOf<String,Car>("fiat500" to Car("fiat","500"),"corolla" to Car("toyota","corolla"),"fiesta" to Car("ford","fiesta"))
            }
            """.trimIndent()
        val formattedCode =
            """
            class Car(val producer:String,val model:String)
            fun main() {
                val a = mapOf("hello" to "world", "hi" to "universe")
                val b:Map<Int,Int> = mapOf(1 to 2,3 to 4)
                val c= mapOf<Int,Int>(3 to 2,1 to 0)
                val d = mapOf<String,Car>("fiat500" to Car("fiat","500"),"corolla" to Car("toyota","corolla"),"fiesta" to Car("ford","fiesta"))
                val a1 = mutableMapOf("hello" to "world", "hi" to "universe")
                val b1:MutableMap<Int,Int> = mutableMapOf(1 to 2,3 to 4)
                val c1= mutableMapOf<Int,Int>(3 to 2,1 to 0)
                val d1 = mutableMapOf<String,Car>("fiat500" to Car("fiat","500"),"corolla" to Car("toyota","corolla"),"fiesta" to Car("ford","fiesta"))
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(MapOfRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
