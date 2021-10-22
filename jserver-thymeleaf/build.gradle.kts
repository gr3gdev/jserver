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
    implementation(project(":jserver-framework"))
    implementation("org.thymeleaf:thymeleaf:3.0.12.RELEASE")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group as String
            artifactId = "jserver-thymeleaf"
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
