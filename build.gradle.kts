buildscript {
    extra.apply {
        set("java.version", "1.8")
        set("mockito.version", "3.3.3")
        set("junit.platform.version", "1.6.1")
        set("junit.version", "5.6.1")
    }
}

group = "com.github.gr3gdev"
version = "1.0.0-SNAPSHOT"

allprojects {

    repositories {
        mavenCentral()
        jcenter()
    }

}
