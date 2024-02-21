plugins {
    id("java")
}

group = "photogrammer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks.test {
    useJUnitPlatform()
}