plugins {
    id("gregdev.gradle.docker") version "1.0.0"
    application
}

java {
    toolchain {
        languageVersion.set(project.ext["javaVersion"] as JavaLanguageVersion)
    }
}

docker {
    imageName = "gr3gdev/jserver"
    platforms = "linux/amd64"
    dependsOn = "installDist"
    args = listOf("-p", "9000:9000", "-d", "-v", "${rootProject.projectDir.absolutePath + "/jserver-cypress/build/libs"}:/apps")
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
