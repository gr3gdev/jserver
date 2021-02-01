plugins {
    kotlin("jvm") version "1.4.10"
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
        copy {
            from("build/resources/main")
            into("build/classes/kotlin/main")
        }
    }
}
