plugins {
    java
    `java-library`
    id("org.springframework.boot")
}

tasks.processResources {
    from(project(":core:service-impl").sourceSets.main.get().resources)
}

dependencies {
    api(project(":api-common"))
    implementation(project(":core:service"))
    runtimeOnly(project(":core:service-impl"))

    implementation("org.hibernate.orm:hibernate-spatial")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}