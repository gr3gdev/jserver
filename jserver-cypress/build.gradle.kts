plugins {
    kotlin("jvm") version "1.4.10"
    id("com.github.node-gradle.node") version "2.2.4"
    id("gregdev.gradle.docker") version "1.0.0"
    application
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation(project(":jserver-security"))
    implementation(project(":jserver-thymeleaf"))
    implementation(project(":jserver-core"))
}

application {
    mainClass.set("com.github.gr3gdev.jserver.test.CypressTestKt")
}

docker {
    imageName = "gr3gdev/jserver-cypress"
    platforms = "linux/amd64"
    dependsOn = "installDist"
    args = listOf("-p", "9000:9000", "-d")
}

tasks {
    compileKotlin {
        dependsOn("buildFrontReact")
        copy {
            from("build/resources/main")
            into("build/classes/kotlin/main")
        }
        copy {
            from("front-react/build")
            into("build/classes/kotlin/main/react")
        }
    }
    withType(Jar::class) {
        doFirst {
            copy {
                from("front-react/build")
                into("build/classes/kotlin/main/react")
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
