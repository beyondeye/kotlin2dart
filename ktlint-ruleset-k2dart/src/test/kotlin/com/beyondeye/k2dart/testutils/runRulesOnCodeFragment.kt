package com.beyondeye.k2dart

import com.beyondeye.k2dart.testutils.CallbackResult
import com.pinterest.ktlint.core.KtLint
import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.RuleSet

fun runRulesOnCodeFragment(
    code: String,
    rulesToTest: List<Rule>,
): String {
    val callbacks = mutableListOf<CallbackResult>()
    val actualFormattedCode = KtLint.format(
        KtLint.ExperimentalParams(
            text = code,
            ruleSets = listOf(
                RuleSet("kotlin-to-dart", *rulesToTest.toTypedArray()),
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
    return actualFormattedCode
}
