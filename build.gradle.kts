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
//    compileOnly ("org.projectlombok:lombok:1.18.34")
//    annotationProcessor ("org.projectlombok:lombok:1.18.34")
//
//    testCompileOnly ("org.projectlombok:lombok:1.18.34")
//    testAnnotationProcessor ("org.projectlombok:lombok:1.18.34")
    
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "ru.almasgali.Main"
}

tasks.test {
    useJUnitPlatform()
}