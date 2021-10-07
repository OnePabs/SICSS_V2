/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.0/userguide/building_java_projects.html
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*
import org.gradle.api.tasks.testing.logging.TestLogEvent.*


plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
	
	// https://mvnrepository.com/artifact/com.sun.net.httpserver/http
	implementation("com.sun.net.httpserver:http:20070405")

    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-lang3:3.12.0")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.0-jre")

    //google JSON simple
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // Use TestNG framework, also requires calling test.useTestNG() below
    testImplementation("org.testng:testng:7.3.0")
}

application {
    // Define the main class for the application.
    mainClass.set("server.Main")
}

tasks.withType<Test> {
    // Use TestNG for unit tests.
    useTestNG()
    testLogging {
        events(FAILED, STANDARD_ERROR, SKIPPED)
        exceptionFormat = FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
        showStandardStreams = true
    }
}
