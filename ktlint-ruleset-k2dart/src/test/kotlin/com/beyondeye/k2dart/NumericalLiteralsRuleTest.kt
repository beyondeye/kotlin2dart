package com.beyondeye.k2dart

import com.beyondeye.k2dart.rules.FinalInsteadOfValRule
import com.beyondeye.k2dart.rules.NumericalLiteralsRule
import com.beyondeye.k2dart.testutils.CallbackResult
import com.pinterest.ktlint.core.KtLint
import com.pinterest.ktlint.core.RuleSet
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class NumericalLiteralsRuleTest {
    @Test
    fun `remove f F suffix in float literals and L l from long literals`() {
        val l:Long = 2L
        val code =
            """
             fun fn() {
                var f:Float = 1f
                val l:Long = 2L
            }
            """.trimIndent()
        //NOTE: DART accept definition of float variables also using an integer literals
        val formattedCode =
            """
             fun fn() {
                var f:Float = 1
                val l:Long = 2
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(NumericalLiteralsRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
