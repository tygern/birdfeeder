dependencies {
    compile("org.springframework:spring-web:$springVersion")
    compile("org.springframework:spring-jdbc:$springVersion")

    testCompile("org.mariadb.jdbc:mariadb-java-client:$mariaDbVersion")
}