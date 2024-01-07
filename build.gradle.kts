import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5"  // Spring Boot version
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    kotlin("kapt") version "1.7.0"
}

group = "demo.kotlin"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // Spring WebFlux and Springdoc
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.5.12")

    // Thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    implementation("org.webjars:webjars-locator:0.45")
    implementation("org.webjars:bootstrap:5.1.3")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")

    // Database (R2DBC, JPA, QueryDSL)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.1.RELEASE") // R2DBC 버전 변경
    implementation("com.querydsl:querydsl-jpa:4.4.0") // QueryDSL 버전 변경
    kapt("com.querydsl:querydsl-apt:4.4.0:jpa") // QueryDSL 버전 변경
    implementation("javax.persistence:javax.persistence-api:2.2")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Kotlin and Coroutines
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.0")

    // Testing
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
