package com.beyondeye.k2dart

import com.beyondeye.k2dart.rules.SemicolonAtEndOfStatementsRule
import com.pinterest.ktlint.core.Rule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SemicolonAtEndOfStatementsTest {
    @Test
    fun `change basic type names`() {
        val code =
            """
            fun main() {
                var a=1
                val b:Double=2
                var c=1+2
                var d=a+b
                var e=cos(d)
                cos(a)
                return a+b
                return cos(sin(a))
                return a ?: b
            }
            """.trimIndent()
        val formattedCode =
            """
             fun main() {
                var a=1;
                val b:Double=2;
                var c=1+2;
                var d=a+b;
                var e=cos(d);
                cos(a);
                return a+b;
                return cos(sin(a));
                return a ?: b;
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(SemicolonAtEndOfStatementsRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}

