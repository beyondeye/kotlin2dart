package com.beyondeye.k2dart


import com.pinterest.ktlint.ruleset.k2dart.rules.BitOpsRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BitOpsRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            fun main()
            {
                val a=8 shl 2
                val b=8 shr 2
                val c=8 ushr 3
                val d=8 and 8
                val e=8 or 4
                val f=8 xor 2
                val g=8.inv()
            }
            """.trimIndent()
        val formattedCode =
            """
            fun main()
            {
                val a=8 << 2
                val b=8 >> 2
                val c=8 >>> 3
                val d=8 & 8
                val e=8 | 4
                val f=8 ^ 2
                val g=8.inv()
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(BitOpsRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
