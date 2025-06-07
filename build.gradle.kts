plugins {
    id("java")

    `maven-publish`
    `java-library`

    id("com.github.johnrengelman.shadow").version("8.1.1")
}

group = "me.xiaoying.logger"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "me.xiaoying"
            artifactId = "logger"
            version = project.version.toString()
        }
    }

    repositories {
        mavenLocal()
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("net.java.dev.jna:jna:5.16.0")
    implementation("net.java.dev.jna:jna-platform:5.16.0")
}