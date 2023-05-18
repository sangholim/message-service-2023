import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "talk.messageService"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("io.kotest:kotest-bom:5.5.5"))
	implementation("org.springframework.boot:spring-boot-starter-rsocket")
	implementation("org.springframework.security:spring-security-rsocket:6.1.0")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.security:spring-security-messaging:6.1.0")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose:6.1.0")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	testImplementation ("io.jsonwebtoken:jjwt-api:0.11.2")
	testImplementation ("io.jsonwebtoken:jjwt-impl:0.11.2")
	testImplementation ("io.jsonwebtoken:jjwt-jackson:0.11.2")
	testImplementation("io.kotest:kotest-runner-junit5")
	testImplementation("io.kotest:kotest-assertions-core")
	testImplementation("io.kotest:kotest-property")
	testImplementation("org.testcontainers:testcontainers:1.17.6")
	testImplementation("org.testcontainers:mongodb:1.18.0")
	testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<BootBuildImage>("bootBuildImage") {
	val dockerUsername = System.getenv("DOCKER_USERNAME")
	val imageName = System.getenv("IMAGE_NAME")
	this.imageName.set(imageName)
	publish.set(true)
	docker {
		publishRegistry {
			username.set(dockerUsername)
			password.set(System.getenv("DOCKER_PASSWORD"))
		}
	}
}
