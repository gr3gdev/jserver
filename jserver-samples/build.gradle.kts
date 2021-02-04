plugins {
    kotlin("jvm") version "1.4.10"
    id("com.github.node-gradle.node") version "2.2.4"
    application
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation(project(":jserver-security"))
    implementation(project(":jserver-core"))
}

application {
    mainClass.set("com.github.gr3gdev.jserver.samples.CypressTestKt")
}

tasks {
    compileKotlin {
        //dependsOn("installFrontReact")
        copy {
            from("build/resources/main")
            into("build/classes/kotlin/main")
        }
        /*
        copy {
            from("front-react/build")
            into("build/classes/kotlin/main/react")
        }
        */
    }
    register("runCypressTestServer", JavaExec::class) {
        group = "Execution"
        description = "Run server for Cypress tests"
        classpath = sourceSets.main.get().runtimeClasspath
        main = "com.github.gr3gdev.jserver.samples.CypressTestKt"
    }
    register("installFrontReact", com.moowork.gradle.node.npm.NpmTask::class) {
        group = "front"
        setWorkingDir(file("front-react"))
        setArgs(listOf("run", "build"))
    }
}
