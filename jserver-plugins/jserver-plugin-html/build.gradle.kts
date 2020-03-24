plugins {
    kotlin("jvm") version "1.3.61"
    jacoco
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":jserver-core"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = rootProject.extra["java.version"] as String
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = rootProject.extra["java.version"] as String
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = true
        html.destination = file("$buildDir/reports/coverage")
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
    }
}
