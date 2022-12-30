package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.FutureInsteadOfSuspendRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class FutureInsteadOfSuspendRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
            class A

            suspend fun fun1():A
            {
                return A()
            }

            suspend fun fun2()=12

            suspend fun anotherAsyncFun():Int {
                return 1
            }
            """.trimIndent()
        val formattedCode =
            """
            class A

            suspend fun fun1():A
            {
                return A()
            }

            suspend fun fun2()=12

            suspend fun anotherAsyncFun():Int {
                return 1
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(FutureInsteadOfSuspendRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
