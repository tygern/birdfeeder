plugins {
    id("org.flywaydb.flyway") version "5.2.4"
}

dependencies {
    runtime("org.mariadb.jdbc:mariadb-java-client:$mariaDbVersion")
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrate") {
    url = "jdbc:mysql://localhost:3306/feed?user=birdfeeder"
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("testMigrate") {
    url = "jdbc:mysql://localhost:3306/feed_test?user=birdfeeder"
}

