import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "me.letsdev"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    configureEach {
        // exclude LOGBACK
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // database
    runtimeOnly("com.h2database:h2")

    // mapstruct
    implementation("org.mapstruct:mapstruct:1.6.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // flyway
    implementation("org.flywaydb:flyway-core:9.22.3")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // error code
    implementation("com.github.merge-simpson:letsdev-error-code-api:0.1.0")

    // password encoder
    implementation("org.springframework.security:spring-security-crypto")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")

    // instance cache
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("io.mockk:mockk:1.13.12")
}

sourceSets {
    test {
        java {
            setSrcDirs(listOf("src/test/kotlin"))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(
            listOf(
                "--enable-preview",
                "-Amapstruct.defaultComponentModel=spring"
            )
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
}

tasks.named<JavaExec>("bootRun") {
    jvmArgs("--enable-preview")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
    }
}