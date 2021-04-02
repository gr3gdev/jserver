buildscript {
    extra.apply {
        set("mockito.version", "3.3.3")
        set("junit.platform.version", "1.6.1")
        set("junit.version", "5.6.1")
    }
}

group = "com.github.gr3gdev"
version = "0.5.1"

allprojects {

    repositories {
        mavenCentral()
        jcenter()
    }

}
