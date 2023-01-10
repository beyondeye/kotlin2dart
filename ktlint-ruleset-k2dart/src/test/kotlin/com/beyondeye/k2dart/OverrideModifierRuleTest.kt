package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.OverrideModifierRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class OverrideModifierRuleTest {
    @Test
    fun `substitute override modifier with Dart ovveride anntation`() {
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
                @override
                fun toString() = "MyClass"
                 @override
                 fun someFun() {
                    print("hi!")
                }
                 @override

                 val zero: Int
                    get() = 0
            }
""".trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(OverrideModifierRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
