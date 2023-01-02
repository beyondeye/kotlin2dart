package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.CompanionObjectRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class CompanionObjectRuleTest {
    @Test
    fun `companion object members transformed into static members`() {
        val code =
            """
            class A
            {
                val a=1;
                fun someFun():Int {return 1}
                companion object {
                    const val someConst:Int=123
                    private fun someStaticFun():Int { return  1 }
                    fun someOtherStaticFun() = "hello"
                    var someVariable:String="userid"
                }
            }
            """.trimIndent()
        val formattedCode =
            """
            class A
            {
                val a=1;
                fun someFun():Int {return 1}
                companion object {
                    const val someConst:Int=123
                    private fun someStaticFun():Int { return  1 }
                    fun someOtherStaticFun() = "hello"
                    var someVariable:String="userid"
                }
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(CompanionObjectRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
