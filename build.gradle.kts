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

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        md.required.set(true) // simple Markdown format
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "17"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
}

detekt {
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    autoCorrect = false
    buildUponDefaultConfig = true
    allRules = false
}

tasks.register("clean").configure {
    delete("build")
}
tasks.register("copyGitHooks", Copy::class.java) {
    description = "Copies the git hooks from /git-hooks to the .git folder."
    group = "git hooks"
    from("$rootDir/scripts/pre-commit")
    into("$rootDir/.git/hooks/")
}
tasks.register("installGitHooks", Exec::class.java) {
    description = "Installs the pre-commit git hooks from /git-hooks."
    group = "git hooks"
    workingDir = rootDir
    commandLine = listOf("chmod")
    args("-R", "+x", ".git/hooks/")
    dependsOn("copyGitHooks")
    doLast {
        logger.info("Git hook installed successfully.")
    }
}
