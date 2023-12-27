plugins {
    id("application")
}

dependencies {
    implementation(project(":spring-web"))
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-test")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
}

application {
    mainClass.set("io.github.syakuis.idp.authorization.AuthorizationApplication")
}

tasks {
    bootJar {
        enabled = true
        launchScript()
    }

    jar {
        enabled = false
    }
}