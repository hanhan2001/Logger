plugins {
    id("java")
    // shadow
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

group = "me.xiaoying.logger"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("net.java.dev.jna:jna:5.16.0")
    implementation("net.java.dev.jna:jna-platform:5.16.0")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to "me.xiaoying.logger.Test")
    }
}