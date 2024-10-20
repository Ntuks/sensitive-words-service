plugins {
	kotlin("jvm") version "2.0.21"
	kotlin("plugin.spring") version "2.0.21"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.flash.interview.messaging"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2023.0.3"
extra["openTelemetryVersion"] = "1.28.0"
extra["springDocVersion"] = "1.7.0"

dependencies {
	// Spring Boot dependencies
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.cloud:spring-cloud-starter-vault-config")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	// Kotlin dependencies for Jackson and Spring
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// OpenTelemetry
	implementation("io.opentelemetry:opentelemetry-api:${property("openTelemetryVersion")}")
	implementation("io.opentelemetry:opentelemetry-sdk:${property("openTelemetryVersion")}")
	implementation("io.opentelemetry:opentelemetry-exporter-otlp:${property("openTelemetryVersion")}")
	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:${property("openTelemetryVersion")}")

	// Springdoc OpenAPI for Swagger
	implementation("org.springdoc:springdoc-openapi-ui:${property("springDocVersion")}")
	implementation("org.springdoc:springdoc-openapi-kotlin:${property("springDocVersion")}")

	// Database dependencies (if applicable)
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}