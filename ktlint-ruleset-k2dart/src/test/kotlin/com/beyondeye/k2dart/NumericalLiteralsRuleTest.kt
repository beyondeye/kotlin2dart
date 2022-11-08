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
        var f:Float = 1f
        val l:Long = 2L
        // *NAIN* *DARIO*  this is a test that run the KTlint.format look at it to check how it works
        val code =
            """
             fun fn() {
                var f:Float = 1f
                val l:Long = 2L
            }
            """.trimIndent()
        val formattedCode =
            """
             fun fn() {
                var f:Float = 1.0
                val l:Long = 2
            }
            """.trimIndent()
        val callbacks = mutableListOf<CallbackResult>()
        val actualFormattedCode = KtLint.format(
            KtLint.ExperimentalParams(
                text = code,
                ruleSets = listOf(
                    RuleSet("kotlin-to-dart",NumericalLiteralsRule()),
                ),
                userData = emptyMap(),
                cb = { e, corrected ->
                    /*
                    callbacks.add(
                        CallbackResult(
                            line = e.line,
                            col = e.col,
                            ruleId = e.ruleId,
                            detail = e.detail,
                            canBeAutoCorrected = e.canBeAutoCorrected,
                            corrected = corrected,
                        ),
                    )
                     */
                },
                script = false,
                editorConfigPath = null,
                debug = false,
            ),
        )
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
