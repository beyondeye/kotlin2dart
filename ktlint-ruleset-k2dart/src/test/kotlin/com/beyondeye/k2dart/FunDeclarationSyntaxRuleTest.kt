package com.beyondeye.k2dart

import com.beyondeye.k2dart.rules.VariableTypeBeforeNameRule
import com.pinterest.ktlint.core.Rule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FunDeclarationSyntaxRuleTest {
    @Test
    fun `change syntax for function declaration`() {
        val code =
            """
            class SomeClass;
            class SomeOtherClass(val a:Double, val b:String)

            fun main(arg1:Double, arg2:Float,arg3:AClass) {
                var a:Double
                val sc = SomeClass()
                val soc = SomeOtherClass(1.0,"one")
                var sc2:SomeClass = SomeClass()
                var soc2:SomeOtherClass = SomeOtherClass(2,"two")
                var d:Double = 1.0
                var f = 1f
                var f2:Float = 1f
                val i:Int = 1
                val i2  = 1
                val l:Long= 2L
                val b:Boolean = true
                if(true) {
                    a=1.0
                } else {
                    a=2.0
                }
            }
            """.trimIndent()
        val formattedCode =
            """
            class SomeClass;
            class SomeOtherClass(val a:Double, val b:String)

            fun main(arg1:Double, arg2:Float,arg3:AClass) {
                Double a
                final sc = SomeClass()
                final soc = SomeOtherClass(1.0,"one")
                SomeClass sc2 = SomeClass()
                SomeOtherClass soc2 = SomeOtherClass(2,"two")
                Double d = 1.0
                var f = 1f
                Float f2 = 1f
                final Int i = 1
                final i2  = 1
                final Long l= 2L
                final Boolean b = true
                if(true) {
                    a=1.0
                } else {
                    a=2.0
                }
            }
            """.trimIndent()
        val rulesToTest= listOf<Rule>(VariableTypeBeforeNameRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}

