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
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("io.fusionauth:fusionauth-jwt:5.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
    implementation(project(":jserver-framework"))
    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:4.0.0")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group as String
            artifactId = "jserver-security"
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
