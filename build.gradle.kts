plugins {
    id("java")
    id("application")
}

group = "ru.almasgali"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

application {
    mainClass = "ru.almasgali.Main"
}

tasks.test {
    useJUnitPlatform()
}