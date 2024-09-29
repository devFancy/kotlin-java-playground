tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(":domain")
    implementation(project(":support:logging"))
    runtimeOnly(project(":storage:db-main"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}