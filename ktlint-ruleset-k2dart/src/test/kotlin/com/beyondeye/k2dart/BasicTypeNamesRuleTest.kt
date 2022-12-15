package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.BasicTypesNamesRule
import com.pinterest.ktlint.core.Rule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BasicTypeNamesRuleTest {
    @Test
    fun `change basic type names`() {
        val code =
            """
            fun main(arg1:Double, arg2:Float) {
                var d:Double = 1.0
                var f:Float = 1f
                val i:Int = 1
                val l:Long= 2L
                val b:Boolean = true
            }
            """.trimIndent()
        val formattedCode =
            """
            fun main(arg1:double, arg2:double) {
                var d:double = 1.0
                var f:double = 1f
                val i:int = 1
                val l:int= 2L
                val b:bool = true
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(BasicTypesNamesRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}

