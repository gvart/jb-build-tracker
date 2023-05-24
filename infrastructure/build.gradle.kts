plugins {
    kotlin("plugin.spring")
}

dependencies {
    val jobrunrStarterVersion: String by project
    val log4jApiKotlinVersion: String by project
    val springDocVersion: String by project
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:$log4jApiKotlinVersion")
    implementation("org.jobrunr:jobrunr-spring-boot-3-starter:$jobrunrStarterVersion")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}