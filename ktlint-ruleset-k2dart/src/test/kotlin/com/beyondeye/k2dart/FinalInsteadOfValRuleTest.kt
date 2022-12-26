package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.FinalInsteadOfValRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class FinalInsteadOfValRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
             fun fn() {
                val v = "var"
                var w = "val"
            }
            """.trimIndent()
        val formattedCode =
            """
             fun fn() {
                final v = "var"
                var w = "val"
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(FinalInsteadOfValRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
