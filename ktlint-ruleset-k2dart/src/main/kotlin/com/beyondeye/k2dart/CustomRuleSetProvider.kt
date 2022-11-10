package com.beyondeye.k2dart

import com.beyondeye.k2dart.rules.BasicTypesNamesRule
import com.beyondeye.k2dart.rules.FinalInsteadOfValRule
import com.beyondeye.k2dart.rules.NumericalLiteralsRule
import com.beyondeye.k2dart.rules.SemicolonAtEndOfStatementsRule
import com.pinterest.ktlint.core.RuleProvider
import com.pinterest.ktlint.core.RuleSetProviderV2

public const val k2dartRulesetId: String = "k2dart"

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
            RuleProvider { FinalInsteadOfValRule() },
        )
}
