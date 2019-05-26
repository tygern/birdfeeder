dependencies {
    compile(project(":components:rss"))

    compile("org.springframework:spring-web:$springVersion")
    compile("org.springframework:spring-jdbc:$springVersion")

    testCompile(project(":components:test-support"))
}