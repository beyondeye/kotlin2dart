package com.beyondeye.k2dart

import com.beyondeye.k2dart.rules.*
import com.pinterest.ktlint.core.RuleProvider
import com.pinterest.ktlint.core.RuleSetProviderV2

public const val k2dartRulesetId: String = "k2dart"

//TODO rename this to K2DartRuleSetProvider (need to rename references also in META-INF.services RuleSetProvider and RuleSetProviderV2
// see also https://pinterest.github.io/ktlint/extensions/custom-rule-set/
// Every time a new k2dart rule is added don't forget to add it in the list below in the definition of getRuleProviders()
public class CustomRuleSetProvider :
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
            RuleProvider { SemicolonAtEndOfStatementsRule() },
            RuleProvider { BasicTypesNamesRule() },
            RuleProvider { NumericalLiteralsRule() },
            //*dario* FinalInsteadOfValRule rule is obsolete, since it has been integrated in VariableTypeBeforeNameRule
            //RuleProvider { FinalInsteadOfValRule() },
            RuleProvider { VariableTypeBeforeNameRule() }
        )
}
