package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.IsEmptyRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class IsEmptyRuleTest {
    @Test
    fun `replace isEmpty isNotEmpty method with property`() {
        val code =
            """
            fun main() {
                val a="hi   "
                val a_is_empty=a.isEmpty()
                val b=""
                val b_not_empty=b.isNotEmpty()

                val vec= listOf(1,2,3)
                val vec_is_empty=vec.isEmpty()
                val vec_not_empty=vec.isNotEmpty()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun main() {
                val a="hi   "
                val a_is_empty=a.isEmpty()
                val b=""
                val b_not_empty=b.isNotEmpty()

                val vec= listOf(1,2,3)
                val vec_is_empty=vec.isEmpty()
                val vec_not_empty=vec.isNotEmpty()
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(IsEmptyRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
