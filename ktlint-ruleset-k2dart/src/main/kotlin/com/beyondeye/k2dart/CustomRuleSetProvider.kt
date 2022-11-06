package com.beyondeye.k2dart

import com.pinterest.ktlint.core.RuleProvider
import com.pinterest.ktlint.core.RuleSetProviderV2

public class CustomRuleSetProvider :
    RuleSetProviderV2(
        id = "dart-basic", //*DARIO* this id is used in order to identify this ruleset and decide if too include it or not
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
            RuleProvider { FinalInsteadOfValRule() },
        )
}
