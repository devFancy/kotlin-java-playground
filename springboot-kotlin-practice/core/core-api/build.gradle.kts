tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":core:core-common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
