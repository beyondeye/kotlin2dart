package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.VisibilityModifiersRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test


class VisibilityModifiersRuleTest {
    @Test
    fun `comment out visibility modifiers`() {
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
            /* private */ val a=1
             /* protected */

             fun someProtectedFun() {
                print("hi")
            }
             /* internal */
             class A {
                 /* private */
                 val a=1;
                 /* protected */
                 fun fn() {
                    print("hello")
                }
            }
             /* public */

             class B {
                 /* internal */
                 var b=0
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(VisibilityModifiersRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
