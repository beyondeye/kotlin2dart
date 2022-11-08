package com.beyondeye.k2dart.testutils

data class CallbackResult(
    val line: Int,
    val col: Int,
    val ruleId: String,
    val detail: String,
    val canBeAutoCorrected: Boolean,
    val corrected: Boolean,
)
