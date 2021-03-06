import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50" apply false
    kotlin("plugin.spring") version "1.3.50" apply false
    id("org.springframework.boot") version "2.1.8.RELEASE" apply false
}

subprojects {
    if (isJavaProject()) {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "kotlin")

        repositories {
            jcenter()
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }

        dependencies {
            "compile"(kotlin("stdlib"))

            "testCompile"("io.damo.aspen:aspen:2.1.0")
            "testCompile"("org.assertj:assertj-core:3.11.1")
            "testCompile"("io.mockk:mockk:1.9.3")
        }
    }
}

fun Project.isJavaProject() = !listOf(
    "applications",
    "components",
    "databases").contains(name)
