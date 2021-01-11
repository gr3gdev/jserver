plugins {
    kotlin("jvm") version "1.4.10"
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.mockito:mockito-core:${rootProject.extra["mockito.version"]}")
    testImplementation("org.mockito:mockito-junit-jupiter:${rootProject.extra["mockito.version"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${rootProject.extra["junit.version"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${rootProject.extra["junit.version"]}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${rootProject.extra["junit.platform.version"]}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${rootProject.extra["junit.version"]}")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:${rootProject.extra["junit.version"]}")
}

tasks {
    compileKotlin {
        copy {
            from("build/resources/main")
            into("build/classes/kotlin/main")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group as String
            artifactId = "jserver-core"
            version = rootProject.version as String
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GithubPackage"
            url = uri("https://maven.pkg.github.com/gr3gdev/jserver")
            credentials {
                username = project.findProperty("gpr.user") as String
                password = project.findProperty("gpr.key") as String
            }
        }
    }
}
