plugins {
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    val jobrunrStarterVersion: String by project
    val log4jApiKotlinVersion: String by project
    val springDocVersion: String by project
    val testContainersVersion: String  by project
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:$log4jApiKotlinVersion")
    implementation("org.jobrunr:jobrunr-spring-boot-3-starter:$jobrunrStarterVersion")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:localstack:$testContainersVersion")


}