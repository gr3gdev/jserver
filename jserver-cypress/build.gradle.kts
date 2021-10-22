plugins {
    id("com.github.node-gradle.node") version "2.2.4"
    java
}

java {
    toolchain {
        languageVersion.set(project.ext["javaVersion"] as JavaLanguageVersion)
    }
}

dependencies {
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.0")
    implementation(project(":jserver-security"))
    implementation(project(":jserver-thymeleaf"))
    implementation(project(":jserver-framework"))
}

tasks {
    withType(Jar::class) {
        dependsOn("installFrontReact")
        doFirst {
            copy {
                from("front-react/build")
                into("build/classes/java/main/react")
            }
        }
    }
    register("installFrontReact", com.moowork.gradle.node.npm.NpmTask::class) {
        group = "front"
        setWorkingDir(file("front-react"))
        setArgs(listOf("install"))
    }
    register("buildFrontReact", com.moowork.gradle.node.npm.NpmTask::class) {
        dependsOn("installFrontReact")
        group = "front"
        setWorkingDir(file("front-react"))
        setArgs(listOf("run", "build"))
    }
    register("cypressRun", com.moowork.gradle.node.npm.NpmTask::class) {
        group = "verification"
        setArgs(listOf("run", "cy:run"))
    }
}
