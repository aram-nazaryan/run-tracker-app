rootProject.name = "RunTrackerApplication"

include(
    ":api",
    ":api-common",
    ":core:persistence-entities",
    ":core:persistence-repositories",
    ":core:common-datatypes",
    ":core:service",
    ":core:service-impl"
)

pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }

    plugins {
        id("org.springframework.boot").version("3.0.0")
    }
}