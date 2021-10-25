plugins {
    id("gregdev.gradle.docker") version "1.0.0"
}

group = "com.github.gr3gdev"
version = "2.0.0"

docker {
    imageName = "gr3gdev/jserver"
    platforms = "linux/amd64"
    dependsOn = ":jserver-core:installDist"
    args = listOf("-p", "9000:9000", "-d", "-v", "${projectDir.absolutePath + "/jserver-cypress/build/libs"}:/apps")
}

allprojects {

    project.ext.set("javaVersion", JavaLanguageVersion.of(11))

    repositories {
        mavenCentral()
    }

}
