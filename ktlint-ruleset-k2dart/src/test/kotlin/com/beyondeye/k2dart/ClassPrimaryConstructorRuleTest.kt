package com.beyondeye.k2dart

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.ruleset.k2dart.rules.ClassPrimaryConstructorRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test


class ClassPrimaryConstructorRuleTest {
    @Test
    fun `change syntax for primary constructor`() {
        val code =
            """
            class A(val a:String,var b:Int)
            """.trimIndent()
        val formattedCode =
            """
            class A/* (val a:String,var b:Int) */
            {
            val a:String
            var b:Int

            A(this.a,this.b,);
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(ClassPrimaryConstructorRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }
    @Test
    fun `change syntax for primary constructor with open modifier`() {
        val code =
            """
            open class A(
                /**
                 * sssss
                 */
                val s: Float=0f,
                /**
                 * eeee
                 */
                val e: Float=0f
            ) {
                fun contains(value: Float): Boolean = value >= s && value <= e
            }
            """.trimIndent()
        val formattedCode =
            """
            open class A/* (
                /**
                 * sssss
                 */
                val s: Float=0f,
                /**
                 * eeee
                 */
                val e: Float=0f
            ) */ {
                /**
                 * sssss
                 */
                val s: Float=0f
            /**
                 * eeee
                 */
                val e: Float=0f

            A(this.s,this.e,);
            fun contains(value: Float): Boolean = value >= s && value <= e
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(ClassPrimaryConstructorRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }
    @Test
    fun `change syntax for primary constructor for data class with empty body`() {
        val code =
            """
            data class B(val lst:List<A> = listOf()) {}
            """.trimIndent()
        val formattedCode =
            """
            data class B/* (val lst:List<A> = listOf()) */ {
            val lst:List<A> = listOf()

            B(this.lst,);
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(ClassPrimaryConstructorRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }
}

