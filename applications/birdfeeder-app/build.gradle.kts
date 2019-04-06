plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    "compile"(project(":components:feeds"))

    "compile"("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
}
