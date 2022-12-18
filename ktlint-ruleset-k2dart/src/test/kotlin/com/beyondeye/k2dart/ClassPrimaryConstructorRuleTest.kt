package com.beyondeye.k2dart

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.ruleset.k2dart.rules.FunDeclarationSyntaxRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test


class ClassPrimaryConstructorRuleTest {
    @Test
    fun `change syntax for basic function declaration`() {
        val code =
            """
            fun fn1(arg1:Double, arg2:Float,arg3:AClass):Double {
                return 1.0
            }
            """.trimIndent()
        val formattedCode =
            """
            Double fn1(Double arg1, Float arg2,AClass arg3) {
                return 1.0
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(FunDeclarationSyntaxRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }
    @Test
    fun `change syntax for basic function with void return value`() {
        val code =
            """
            fun fn1(arg1:Double, arg2:Float,arg3:AClass) {

            }
            """.trimIndent()
        val formattedCode =
            """
            void fn1(Double arg1, Float arg2,AClass arg3) {

            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(FunDeclarationSyntaxRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }
    @Test
    fun `change syntax for basic function with single value`() {
        val code =
            """
            fun fn1(arg1:Double, arg2:Double):Double = arg1+arg2
            """.trimIndent()
        val formattedCode =
            """
            Double fn1(Double arg1, Double arg2) => arg1+arg2
            """.trimIndent()
        val rulesToTest= listOf<Rule>(FunDeclarationSyntaxRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}

