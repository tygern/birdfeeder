plugins {
    kotlin("jvm") version "1.3.21" apply false
    kotlin("plugin.spring") version "1.3.21" apply false
    id("org.springframework.boot") version "2.1.3.RELEASE" apply false
}

subprojects {
    if (isJavaProject()) {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "kotlin")

        repositories {
            jcenter()
        }

        dependencies {
            "compile"(kotlin("stdlib"))

            "testCompile"("io.damo.aspen:aspen:2.1.0")
            "testCompile"("org.assertj:assertj-core:3.11.1")
        }
    }
}

fun Project.isJavaProject() = !listOf(
    "applications",
    "components",
    "databases").contains(name)
