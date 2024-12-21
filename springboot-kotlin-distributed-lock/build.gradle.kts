plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.be"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-aop")

	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	// https://docs.spring.io/spring-data/redis/docs/3.1.6/reference/html/#redis:connectors:connection
	// 공식문서에서 connection pooling을 위해서 commons-pool 2를 사용하라고 명시되어있음.
	implementation("org.apache.commons:commons-pool2:2.12.0")

	// distributed lock
	implementation("org.redisson:redisson-spring-boot-starter:3.31.0")

	// logging
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

	// r2dbc
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("io.r2dbc:r2dbc-h2:1.0.0.RELEASE")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("io.netty:netty-resolver-dns-native-macos:4.1.68.Final:osx-aarch_64")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
