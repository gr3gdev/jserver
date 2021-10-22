group = "com.github.gr3gdev"
version = "2.0.0"

allprojects {

    project.ext.set("javaVersion", JavaLanguageVersion.of(15))

    repositories {
        mavenCentral()
    }

}
