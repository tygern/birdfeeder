import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    compile(project(":components:feeds"))

    compile("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    compile("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")

    compile("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    runtime("org.mariadb.jdbc:mariadb-java-client:$mariaDbVersion")
}

tasks.withType<BootRun> {
    environment("SPRING_DATASOURCE_URL", "jdbc:mariadb://localhost:3306/feed?user=birdfeeder")
}

tasks.withType<Test> {
    environment("SPRING_DATASOURCE_URL", "jdbc:mariadb://localhost:3306/feed_test?user=birdfeeder")
}
