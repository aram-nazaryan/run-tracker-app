plugins {
    `java-library`
}

dependencies {
    implementation(project(":core:common-datatypes"))

    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.hibernate.orm:hibernate-spatial")
    implementation("io.hypersistence:hypersistence-utils-hibernate-60:3.1.1")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.data:spring-data-jpa")
    implementation("com.fasterxml.jackson.core:jackson-annotations")

}
