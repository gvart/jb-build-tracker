rootProject.name = "build-tracker"

include(
    "domain",
    "infrastructure"
)

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val spotlessVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
        id("com.diffplug.spotless") version spotlessVersion
    }
}
