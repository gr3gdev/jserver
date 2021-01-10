plugins {
    kotlin("jvm") version "1.4.10"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation(project(":jserver-security"))
    implementation(project(":jserver-core"))
}

tasks {
    compileKotlin {
        copy {
            from("build/resources/main")
            into("build/classes/kotlin/main")
        }
    }
}