dependencies {
    compile(project(":components:result"))

    compile("org.springframework:spring-web:$springVersion")
    compile("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testCompile(project(":components:test-support"))
    testCompile("org.springframework:spring-test:$springVersion")
}
