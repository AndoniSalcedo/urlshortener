import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    //id("org.springframework.boot") version "2.5.5" //apply false
    //id("io.spring.dependency-management") version "1.0.11.RELEASE" //apply false
    //kotlin("plugin.spring") version "1.5.31" //apply false

    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.3.72"
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    
}

group = "com.unizar"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:0.9.0")
    implementation ("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
    
    implementation ("org.glassfish.jaxb:jaxb-runtime:2.3.2")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    implementation("com.google.guava:guava:23.0")
	implementation ("com.google.zxing:core:3.4.0")
    implementation("com.google.zxing:javase:3.3.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.apache.httpcomponents:httpclient")
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test>{
    useJUnitPlatform()
}
