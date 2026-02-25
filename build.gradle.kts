import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktLint)
    alias(libs.plugins.detekt)
}

detekt {
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    autoCorrect = false
    buildUponDefaultConfig = true
    allRules = false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        android = true
        outputColorName = "RED"
        ignoreFailures = false
        debug = true
        verbose = true
        android = false
        outputToConsole = true

        filter {
            exclude("**/generated/**")
            exclude("**/build/**")
        }
    }
}
