plugins {
    id("java")
}

group = "com.fgiannesini"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jocl:jocl:2.0.5")

    testImplementation("org.openjdk.jmh:jmh-core:1.36")
    testAnnotationProcessor ("org.openjdk.jmh:jmh-generator-annprocess:1.36")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}