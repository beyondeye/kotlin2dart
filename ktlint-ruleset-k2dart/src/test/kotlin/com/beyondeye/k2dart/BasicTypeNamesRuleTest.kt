package com.beyondeye.k2dart

import com.beyondeye.k2dart.rules.FinalInsteadOfValRule
import com.beyondeye.k2dart.testutils.CallbackResult
import com.pinterest.ktlint.core.KtLint
import com.pinterest.ktlint.core.RuleSet
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BasicTypeNamesRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        // *NAIN* *DARIO*  this is a test that run the KTlint.format look at it to check how it works
        val code =
            """
             fun fn() {
                var v = "var"
            }
            """.trimIndent()
        val formattedCode =
            """
             fun fn() {
                final v = "var"
            }
            """.trimIndent()
        val callbacks = mutableListOf<CallbackResult>()
        val actualFormattedCode = KtLint.format(
            KtLint.ExperimentalParams(
                text = code,
                ruleSets = listOf(
                    RuleSet("kotlin-to-dart", FinalInsteadOfValRule()),
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
        /*
        Assertions.assertThat(callbacks).containsExactly(
            CallbackResult(
                line = 1,
                col = 12,
                ruleId = "auto-correct",
                detail = AutoCorrectErrorRule.ERROR_MESSAGE_CAN_NOT_BE_AUTOCORRECTED,
                canBeAutoCorrected = false,
                corrected = false,
            ),
            CallbackResult(
                line = 2,
                col = 12,
                ruleId = "auto-correct",
                detail = AutoCorrectErrorRule.ERROR_MESSAGE_CAN_BE_AUTOCORRECTED,
                canBeAutoCorrected = true,
                corrected = true,
            ),
        )
         */
    }


}
