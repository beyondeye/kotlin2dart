package com.pinterest.ktlint.ruleset.k2dart

import com.pinterest.ktlint.core.RuleProvider
import com.pinterest.ktlint.core.RuleSetProviderV2
import com.pinterest.ktlint.ruleset.k2dart.rules.*

public const val k2dartRulesetId: String = "ktodart"

//TODO rename this to K2DartRuleSetProvider (need to rename references also in META-INF.services RuleSetProvider and RuleSetProviderV2
// see also https://pinterest.github.io/ktlint/extensions/custom-rule-set/
// Every time a new k2dart rule is added don't forget to add it in the list below in the definition of getRuleProviders()
public class K2DartRuleSetProvider :
    RuleSetProviderV2(
        id = k2dartRulesetId, //*DARIO* this id is used in order to identify this ruleset and decide if to include it or not
        about = About(
            maintainer = "Dario Elyasy",
            description = "the basic rules set to produce dart code from kotlin code",
            license = "https://github.com/beyondeye/kotlin2dart/blob/master/LICENSE",
            repositoryUrl = "https://github.com/beyondeye/kotlin2dart",
            issueTrackerUrl = "https://github.com/beyondeye/kotlin2dart/issues",
        ),
    ) {
    override fun getRuleProviders(): Set<RuleProvider> =
        setOf(
            /**
             * this is a very simple rule that add an empty class body for classes that do not declare one
             * it is required for many other rules like [ClassPrimaryConstructorRule], [DataClassesRule] to work
             * and transform the AST that is compatible will all other rules, so we want to run it early
             */
            RuleProvider(4) { MissingClassBodyRule() },

            /**
             * this is a very simple rule and we want to run it early so that other rules that manipulate type are
             * easier to implement, if we assume we don't need to maintain the exact AST tree structure that is output by
             * the kotlin compiler
             */
            RuleProvider(3) { BasicTypesNamesRule() },

            /**
             * the companion object rule that transform companion object members into static members take advantage
             * of higher level structure of the AST generated by the kotlin compiler, so we want it to run early
             * before other rules that could modify that structure
             */
            RuleProvider(3) { CompanionObjectRule() },

            /**
             * this rule should be run quite early to make it easier for further rule to work
             */
            RuleProvider( 2) { VisibilityModifiersRule() },
            /**
             * this rule should be run before [ClassPrimaryConstructorRule]
             */
            RuleProvider (2){ DataClassesRule() },
            /**
             * this rule should run before VariableTypeBeforeNameRule so we assign to it higher priority
             */
            RuleProvider(1) { ClassPrimaryConstructorRule() },
            /**
             * this rule should be run before FunDeclarationSyntaxRule
             */
            RuleProvider(1) { FutureInsteadOfSuspendRule() },
            RuleProvider { SemicolonAtEndOfStatementsRule() },
            RuleProvider { NumericalLiteralsRule() },
            //*dario* FinalInsteadOfValRule rule is obsolete, since it has been integrated in VariableTypeBeforeNameRule
            //RuleProvider { FinalInsteadOfValRule() },
            RuleProvider { VariableTypeBeforeNameRule() },
            RuleProvider { FunDeclarationSyntaxRule() },
            RuleProvider { OverrideModifierRule() },
            RuleProvider { BitOpsRule() },
            RuleProvider { NullOpsRule() },
            RuleProvider { IsEmptyRule() },
            RuleProvider { MathOpsRule() },
            RuleProvider { ForLoopIndicesRule() },
        )
}
