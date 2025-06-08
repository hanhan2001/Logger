plugins {
    id("java")

    `maven-publish`
    `java-library`

    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.xiaoying.logger"
version = "1.0.0"

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("net.java.dev.jna:jna:5.16.0")
    implementation("net.java.dev.jna:jna-platform:5.16.0")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options {
            encoding = "UTF-8"
            (this as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
        }
    }

    named<Jar>("jar") {
        enabled = false
    }

    shadowJar {
        archiveFileName.set("logger-${project.version}.jar")
        archiveClassifier.set("")
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.xiaoying"
            artifactId = "logger"
            version = rootProject.version.toString()

            artifact(tasks.shadowJar) {
                classifier = ""
            }

            artifact(tasks["sourcesJar"]) {
                classifier = "sources"
            }

            artifact(tasks["javadocJar"]) {
                classifier = "javadoc"
            }

            pom {
                name.set("Logger Library")
                url.set("https://github.com/hanhan2001/logger")
                developers {
                    developer {
                        id.set("xiaoying")
                        name.set("Xiao Ying")
                        email.set("764932129@qq.com")
                    }
                }
            }
        }
    }

    repositories {
        mavenLocal()
    }
}