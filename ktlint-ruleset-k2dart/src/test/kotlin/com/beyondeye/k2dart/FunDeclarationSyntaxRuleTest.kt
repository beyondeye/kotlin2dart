package com.beyondeye.k2dart

import com.beyondeye.k2dart.rules.VariableTypeBeforeNameRule
import com.pinterest.ktlint.core.Rule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

//
class FunDeclarationSyntaxRuleTest {
    @Test
    fun `change syntax for function declaration`() {
        val code =
            """
            fun fn1(arg1:Double, arg2:Float,arg3:AClass):Double {
                return 1.0
            }
            """.trimIndent()
        val formattedCode =
            """
            Double fn1(Double arg1,Float arg2,AClass arg3) {
                return 1.0
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(VariableTypeBeforeNameRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}

