plugins {
    `java-library`
}

dependencies {
    implementation(project(":core:service"))
    implementation(project(":core:persistence-repositories"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.hibernate.orm:hibernate-spatial")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
