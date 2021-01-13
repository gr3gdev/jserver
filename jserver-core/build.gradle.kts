plugins {
    kotlin("jvm") version "1.4.10"
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit:junit:4.13")
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
                username = System.getProperty("GITHUB_USERNAME") as String? ?: System.getenv("GITHUB_USERNAME")
                password = System.getProperty("GITHUB_TOKEN") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
