import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    id("com.diffplug.spotless")
    jacoco
    java
}


repositories {
    mavenCentral()
}


subprojects {
    group = "md.gvart"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
    }

    dependencies {
        val springBootVersion: String by project
        val springCloudAwsVersion: String by project
        val dataFakerVersion: String by project
        val mockitoKotlinVersion: String by project
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:$springCloudAwsVersion"))

        implementation("org.jetbrains.kotlin:kotlin-reflect")

        testImplementation("net.datafaker:datafaker:$dataFakerVersion")
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
        testImplementation("org.mockito:mockito-core")
        testImplementation("org.assertj:assertj-core")
    }


    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    spotless {
        kotlin {
            ktfmt()
        }
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
}