package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.OverrideModifierRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class OverrideModifierRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            interface MyInterface {
                fun someFun()
                val zero:Int
            }


            class MyClass : MyInterface {
                @Override
                fun toString() = "MyClass"
                override fun someFun() {
                    print("hi!")
                }

                override val zero: Int
                    get() = 0
            }
""".trimIndent()
        val formattedCode =
            """
            interface MyInterface {
                fun someFun()
                val zero:Int
            }


            class MyClass : MyInterface {
                @Override
                fun toString() = "MyClass"
                override fun someFun() {
                    print("hi!")
                }

                override val zero: Int
                    get() = 0
            }
""".trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(OverrideModifierRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
