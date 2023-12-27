dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-undertow")
    api("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}
