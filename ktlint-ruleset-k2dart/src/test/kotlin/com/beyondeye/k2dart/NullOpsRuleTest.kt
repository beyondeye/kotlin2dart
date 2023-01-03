package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.NullOpsRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class NullOpsRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            var a:Int?=null
            val b:Int= a ?: 1
            a = 2
            c = a!!
            print(a!!)
            """.trimIndent()
        val formattedCode =
            """
            var a:Int?=null
            val b:Int= a ?: 1
            a = 2
            c = a!!
            print(a!!)
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(NullOpsRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
