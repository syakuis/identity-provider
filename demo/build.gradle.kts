plugins {
    id("application")
}

dependencies {
    implementation(project(":spring-web"))
    developmentOnly("org.springframework.boot:spring-boot-devtools")

//    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-test")

    implementation ("org.springframework.boot:spring-boot-starter-webflux")
}

application {
    mainClass.set("io.github.syakuis.idp.demo.DemoApplication")
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