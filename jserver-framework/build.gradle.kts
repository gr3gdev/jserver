plugins {
    `maven-publish`
    `java-library`
}

java {
    toolchain {
        languageVersion.set(project.ext["javaVersion"] as JavaLanguageVersion)
    }
}

dependencies {
    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:4.0.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group as String
            artifactId = "jserver-framework"
            version = rootProject.version as String
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GithubPackage"
            url = uri("https://maven.pkg.github.com/gr3gdev/jserver")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getProperty("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getProperty("GITHUB_TOKEN")
            }
        }
    }
}
