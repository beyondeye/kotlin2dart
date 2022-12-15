package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.SemicolonAtEndOfStatementsRule
import com.pinterest.ktlint.core.Rule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SemicolonAtEndOfStatementsTest {
    @Test
    fun `add semicolons where needed`() {
        val code =
            """
            |fun main() {
            |    var s= "someString"
            |    var s2= "${'$'}{s}" + "s2"
            |    var aa=12 ;
            |    var a=1
            |    val b:Double=2
            |    var c=1+2
            |    var d=a+b
            |    var e=cos(d)
            |    cos(a)
            |    return a+b
            |    return cos(sin(a))
            |    return a ?: b
            |}
            """.trimMargin()
        val formattedCode =
            """
            |fun main() {
            |    var s= "someString";
            |    var s2= "${'$'}{s}" + "s2";
            |    var aa=12 ;
            |    var a=1;
            |    val b:Double=2;
            |    var c=1+2;
            |    var d=a+b;
            |    var e=cos(d);
            |    cos(a);
            |    return a+b;
            |    return cos(sin(a));
            |    return a ?: b;
            |}
            """.trimMargin()
        val rulesToTest= listOf<Rule>(SemicolonAtEndOfStatementsRule())
        val actualFormattedCode = runRulesOnCodeFragment(code, rulesToTest)
        Assertions.assertThat(actualFormattedCode).isEqualTo(formattedCode)
    }


}

