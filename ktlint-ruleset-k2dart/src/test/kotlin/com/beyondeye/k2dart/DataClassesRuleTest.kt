package com.beyondeye.k2dart

import com.pinterest.ktlint.ruleset.k2dart.rules.DataClassesRule
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DataClassesRuleTest {
    @Test
    fun `generate stubs for data classes autogenerated methods`() {
        val code =
            """
            class SomeClass

            data class A(val a:Int, val b:String, var c:SomeClass)
            {
                fun someMethod() { print("Hi") }
            }
            """.trimIndent()
        val formattedCode =
            """
            class SomeClass

            /* data */ class A(val a:Int, val b:String, var c:SomeClass)
            {

            A copyWith({
               Int? a,
               String? b,
               SomeClass? c,
            }) {
              return A(
                a: a ?? this.a,
                b: b ?? this.b,
                c: c ?? this.c,
              );
            }

            // AUTOGENERATED METHOD
            //TODO for maps substitute <field>==other.<field> with mapEquals(<field>,other.<field>
            //TODO for lists substitute <field>==other.<field> with listEquals(<field>,other.<field>
            @override
            bool operator ==(Object other) =>
                 identical(this, other) ||
                 other is A &&
                 runtimeType == other.runtimeType &&
                 a == other.a &&
                 b == other.b &&
                 c == other.c;

            @override
            int get hashCode => a.hashCode ^ b.hashCode ^ c.hashCode;
            fun someMethod() { print("Hi") }
            }
            """.trimIndent()
        val actualFormattedCode = runRulesOnCodeFragment(code, listOf(DataClassesRule()))
        Assertions.assertThat(actualFormattedCode).isEqualToIgnoringWhitespace(formattedCode)
    }


}
