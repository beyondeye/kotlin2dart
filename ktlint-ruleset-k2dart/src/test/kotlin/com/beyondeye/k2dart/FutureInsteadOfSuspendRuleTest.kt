package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.FutureInsteadOfSuspendRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FutureInsteadOfSuspendRuleTest {
    @Test
    fun `comment out suspend keyword and change return type to future`() {
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

            /* suspend */ fun fun1():Future<A>
            {
                return A()
            }

            /* suspend */ fun fun2():Future<Object> =12

            /* suspend */ fun anotherAsyncFun():Future<Int> {
                return 1
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(FutureInsteadOfSuspendRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
