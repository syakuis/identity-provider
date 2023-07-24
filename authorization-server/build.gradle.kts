dependencies {
    implementation(project(":core-web"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-test")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

application {
    mainClass.set("io.github.syakuis.idp.authorization.AuthorizationApplication")
}