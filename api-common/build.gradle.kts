dependencies {
    api(project(":core:common-datatypes"))

    implementation("org.springframework.hateoas:spring-hateoas")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.apache.commons:commons-lang3")
}

