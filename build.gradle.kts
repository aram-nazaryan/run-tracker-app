description = "tunTrackerApp"
val gradleWrapperVersion = "7.5.1"

plugins {
    jacoco
    `java-library`
    id("org.springframework.boot")
}

group = "am.run.tracker"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    gradlePluginPortal()
}

allprojects {
    apply(plugin = "io.spring.dependency-management")

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    configurations {
        all {
            resolutionStrategy {
                cacheChangingModulesFor(0, "seconds") // Do not cache SNAPSHOT dependecies
                failOnVersionConflict()
            }
        }
    }
}


subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.spring.dependency-management")

    tasks.register<DependencyReportTask>("allDependencies") {}

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }
    configurations {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

}

springBoot {
    mainClass.set("am.run.tracker.api.RunTrackerApplication")
}


tasks {
    getByName<Wrapper>("wrapper") {
        gradleVersion = gradleWrapperVersion
    }
}

tasks.test {
    useJUnitPlatform()
}

