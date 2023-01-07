package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.GetterAndSetterRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
// *DARIO* this for collecting result of a lint of operation

class GetterAndSetterRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
class A {
    val a:String get() { return "Hi"}
    val b:String get() = "Hello"

    var c:Int=0
        set(value) {
            field=value
        }

    private var _d:Int=0
    var d
        set(value) {
            _d=value
        }
        get() = _d

}

fun main()
{

    val a=A()
    println(a.a)
    println(a.b)
    a.c=1
    println(a.c)

    a.d=3
    println(a.d)

}
""".trimIndent()
        val formattedCode =
            """
class A {
    val a:String get() { return "Hi"}
    val b:String get() = "Hello"

    var c:Int=0
        set(value) {
            field=value
        }

    private var _d:Int=0
    var d
        set(value) {
            _d=value
        }
        get() = _d

}

fun main()
{

    val a=A()
    println(a.a)
    println(a.b)
    a.c=1
    println(a.c)

    a.d=3
    println(a.d)

}
""".trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(GetterAndSetterRule()))
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}
