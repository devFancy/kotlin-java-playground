import org.gradle.api.GradleException
import java.text.SimpleDateFormat

plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

// `customer` 속성이 없거나 빈 값일 경우 기본값을 'default'로 설정
val customer: String = (project.findProperty("customer") as String?).takeIf { !it.isNullOrBlank() } ?: "default"
val dependenciesDir = "gradle/dependencies"
val customerDependenciesFile = file("${dependenciesDir}/${customer}.gradle")

// 공통 및 고객사별 속성 값 로드
val kotlinVersion: String = project.findProperty("kotlinVersion") as String
val javaVersion: String = project.findProperty("javaVersion") as String
val springBootVersion: String = project.findProperty("${customer}SpringBootVersion") as String? ?: project.findProperty("springBootVersion") as String
val springDependencyManagementVersion: String = project.findProperty("${customer}SpringDependencyManagementVersion") as String? ?: project.findProperty("springDependencyManagementVersion") as String

// 빌드 정보 출력
val buildDate = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(System.currentTimeMillis())
println("[${customer.uppercase()}] profiles have been activated.")
println("Kotlin version: $kotlinVersion, Java version: $javaVersion")

// 프로젝트 공통 설정
group = project.findProperty("projectGroup") as String
version = project.findProperty("applicationVersion") as String

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
	}
}

// 의존성 파일 로드
if (customerDependenciesFile.exists()) {
	println("Loading dependency file: $customerDependenciesFile")
	apply(from = customerDependenciesFile)
} else {
	throw GradleException("Unknown customer profile: $customer. File $customerDependenciesFile does not exist.")
}

repositories {
	mavenCentral()
}

dependencies {
	// Kotlin Reflect
	implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")

	// Spring Boot Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:${kotlinVersion}")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher:${springBootVersion}")
}

// 리소스 처리 작업
tasks.named<ProcessResources>("processResources") {
	from("src/main/resources") {
		include("**/application.yml")
		filter { line: String ->
			line.replace("@appVersion@", "BuildDate: $buildDate")
		}
	}
}

// Kotlin DSL 기본값: Gradle 7.x 이상부터는 기본적으로 UTF-8 인코딩이 설정되므로 아래 부분은 주석처리/제거해도 됨
tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

// 테스트 플랫폼 설정
tasks.withType<Test> {
	useJUnitPlatform()
}
