package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.MissingClassBodyRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class MissingClassBodyRuleTest {
    @Test
    fun `create an empty class body if missing`() {
        val code =
            """
            class A

            class B(val a:Int,var b:Int)

            data class C(var a:Int,val b:String)

            class C
            {

            }
            """.trimIndent()
        val formattedCode =
            """
            class A
            {
            }


            class B(val a:Int,var b:Int)
            {
            }


            data class C(var a:Int,val b:String)
            {
            }


            class C
            {

            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(MissingClassBodyRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
