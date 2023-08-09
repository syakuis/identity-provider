import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springCloudVersion = "2022.0.1"

val h2Version = "1.4.200"

val kotlinVersion = "1.8.22"
val kotestVersion = "5.6.2"

val restDocsVersion = "3.0.0"
val snippetsDir by extra { file("$buildDir/generated-snippets") }

plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.spring") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.8.22"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.22"
    id("io.kotest") version "0.4.10"
}

tasks {
    wrapper {
        gradleVersion = "7.6"
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")


    group = "io.github.syakuis"
    version = "2.0.0-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "17"
            }
        }
    }

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "application")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.asciidoctor.jvm.convert")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "io.kotest")

    val asciidoctorExtensions: Configuration by configurations.creating

    allOpen {
        annotation("javax.persistence.Entity")
        annotation("javax.persistence.MappedSuperclass")
        annotation("javax.persistence.Embeddable")
    }

    configurations {
        implementation {
            exclude(module = "spring-boot-starter-tomcat")
        }
    }

    tasks.processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    dependencies {
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-undertow")

        asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor:$restDocsVersion")
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:$restDocsVersion")
        api("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")

        testImplementation("com.h2database:h2:$h2Version")
        compileOnly("com.h2database:h2:$h2Version")

        implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
        implementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.4")

        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
        testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
        testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    }

    tasks {
        asciidoctor {
            inputs.dir(snippetsDir)
            configurations(asciidoctorExtensions.name)
            dependsOn("test")
        }

        test {
            outputs.dir(snippetsDir)
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
            systemProperty("spring.profiles.active", "test")
        }

        bootJar {
            enabled = true
            launchScript()
            dependsOn(asciidoctor)
            from(asciidoctor.get().outputDir) {
                into("BOOT-INF/classes/static/api-docs")
            }
        }

        jar {
            enabled = false
        }
    }
}
