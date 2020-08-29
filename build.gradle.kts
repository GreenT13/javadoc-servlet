plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("java")
    id("jacoco")
    id("checkstyle")
    id("com.github.spotbugs") version "4.5.0"
}

group = "com.apon"
version = "0.0.1-SNAPSHOT"
java {
    sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:29.0-jre")
    implementation("com.github.spotbugs:spotbugs-annotations:4.1.1")
    implementation("commons-codec:commons-codec:1.14")
    implementation("org.apache.commons:commons-lang3:3.11")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.4.2")
}

tasks.test {
    useJUnitPlatform()
}

// Configure JaCoCo.
tasks.check {
    // Reports are always generated after running the checks.
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    // Tests are required before generating the report.
    dependsOn(tasks.test)
}

tasks.jacocoTestReport {
    reports {
        // Codecov.io depends on xml format report.
        xml.isEnabled = true
        // Add HTML report readable by humans.
        html.isEnabled = true
    }
}

// Display final report as HTML.
spotbugs {
    tasks.spotbugsMain {
        reports.create("html") {
            isEnabled = true
            setStylesheet("fancy-hist.xsl")
        }
    }
    tasks.spotbugsTest {
        reports.create("html") {
            isEnabled = true
            setStylesheet("fancy-hist.xsl")
        }
    }
}
