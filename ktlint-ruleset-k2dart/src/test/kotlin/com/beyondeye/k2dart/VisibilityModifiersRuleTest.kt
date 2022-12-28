package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.FinalInsteadOfValRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

// *DARIO* this for collecting result of a lint of operation

class VisibilityModifiersRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            private val a=1

            protected fun someProtectedFun() {
                print("hi")
            }
            internal class A {
                private val a=1;
                protected fun fn() {
                    print("hello")
                }
            }

            public class B {
                internal var b=0
            }
            """.trimIndent()
        val formattedCode =
            """
            private val a=1

            protected fun someProtectedFun() {
                print("hi")
            }
            internal class A {
                private val a=1;
                protected fun fn() {
                    print("hello")
                }
            }

            public class B {
                internal var b=0
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(FinalInsteadOfValRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
