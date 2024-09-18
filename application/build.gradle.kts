plugins {
    `java-conventions`
    `java-library`
}

group = "br.com.fullcycle.application"

dependencies {
    implementation(project(":domain"))

    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
}