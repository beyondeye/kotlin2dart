package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.GetterAndSetterRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class GetterAndSetterRuleTest {
    @Test
    fun `change val keyword to final keyword`() {
        val code =
            """
class A {
    val a:String get() { return "Hi"}
    val b:String get() = "Hello"

    var c:Int=0
        get() = field
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

""".trimIndent()
        val formattedCode =
            """
class A {
    final String get a  { return "Hi"}
    final String get b  => "Hello"

    Int get c=0
         => field
        set(value) {
            field=value
        }

    private var _d:Int=0
    var get d
         => _d
set d (value) {
            _d=value
        }


}

""".trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(GetterAndSetterRule()))
        Assertions.assertThat(actualFormattedCode).isEqualToIgnoringWhitespace(formattedCode)
    }


}
