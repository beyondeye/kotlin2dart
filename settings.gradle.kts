pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    `gradle-enterprise`
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

rootProject.name = "ktlint-root"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":ktlint",
    ":ktlint-core",
    ":ktlint-reporter-baseline",
    ":ktlint-reporter-checkstyle",
    ":ktlint-reporter-format",
    ":ktlint-reporter-json",
    ":ktlint-reporter-sarif",
    ":ktlint-reporter-html",
    ":ktlint-reporter-plain",
    ":ktlint-ruleset-experimental",
    ":ktlint-ruleset-standard",
    ":ktlint-ruleset-template",
    ":ktlint-ruleset-test",
    ":ktlint-ruleset-k2dart",
    ":ktlint-test",
    ":ktlint-test-logging",
)
