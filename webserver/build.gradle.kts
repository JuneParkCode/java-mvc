plugins {
    id("java")
}

group = "photogrammer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("com.google.guava:guava:18.0")
    // test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
}

tasks.test {
    useJUnitPlatform()
}