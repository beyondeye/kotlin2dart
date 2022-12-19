package com.beyondeye.k2dart

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.ruleset.k2dart.rules.ClassPrimaryConstructorRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

//
class ClassPrimaryConstructorRuleTest {
    @Test
    fun `change syntax for basic function declaration`() {
        val code =
            """
            class A(val a:String,var b:Int)
            """.trimIndent()
        val formattedCode =
            """
            class A {
                val a:String
                var b:Int
                A(this.a,this.b)
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(ClassPrimaryConstructorRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}

