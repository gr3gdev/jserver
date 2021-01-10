plugins {
    kotlin("jvm") version "1.4.10"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.2")
    implementation(project(":jserver-core"))
    testImplementation("junit:junit:4.13")
}
