package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.MathOpsRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class MathOpsRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            import kotlin.math.ceil
            import kotlin.math.round
            import kotlin.math.abs

            fun main() {
                val a1= Math.round(1.2)
                val a2 = round(1.2)

                val b1= Math.ceil(2.3)
                val b2= ceil(2.3)

                val c1= Math.abs(3.4)
                val c2=abs(3.4)
            }
            """.trimIndent()
        val formattedCode =
            """
            import kotlin.math.ceil
            import kotlin.math.round
            import kotlin.math.abs

            fun main() {
                val a1= Math.round(1.2)
                val a2 = round(1.2)

                val b1= Math.ceil(2.3)
                val b2= ceil(2.3)

                val c1= Math.abs(3.4)
                val c2=abs(3.4)
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(MathOpsRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
