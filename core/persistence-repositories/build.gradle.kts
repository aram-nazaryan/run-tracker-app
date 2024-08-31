plugins {
    `java-library`
}

dependencies {
    implementation(project(":core:common-datatypes"))
    implementation(project(":core:persistence-entities"))

    implementation("org.slf4j:slf4j-api")

    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.hibernate.orm:hibernate-spatial")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
