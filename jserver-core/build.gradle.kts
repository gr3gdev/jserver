plugins {
    application
}

java {
    toolchain {
        languageVersion.set(project.ext["javaVersion"] as JavaLanguageVersion)
    }
}

application {
    mainClass.set("com.github.gr3gdev.jserver.JServerApp")
}

dependencies {
    implementation(project(":jserver-framework"))
    implementation(project(":jserver-thymeleaf"))
    implementation(project(":jserver-security"))
    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:4.0.0")
}

tasks {
    compileJava {
        doLast {
            val propertyFile = file("$projectDir/src/main/resources/version.properties")
            propertyFile.writeText("version=${rootProject.version}")
        }
    }
    register("runForTest", JavaExec::class) {
        dependsOn(":jserver-cypress:build")
        environment("DEV_MODE", 1)
        jvmArgs("-DappsDir=${File(rootProject.projectDir, "jserver-cypress/build").absolutePath}")
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("com.github.gr3gdev.jserver.JServerApp")
    }
}
