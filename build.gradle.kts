plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("java")
    id("war")
    id("jacoco")
    id("com.github.kt3k.coveralls") version "2.10.1"
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
    providedCompile("com.github.spotbugs:spotbugs-annotations:4.1.1")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.test {
    useJUnitPlatform()
}

// Configure JaCoCo.
tasks.test {
    // Reports are always generated after running tests.
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    // Tests are required before generating the report.
    dependsOn(tasks.test)
}

tasks.jacocoTestReport {
    reports {
        // Coveralls plugin depends on xml format report.
        xml.isEnabled = true
        // Add HTML report readable by humans.
        html.isEnabled = true
    }
}

coveralls {
    jacocoReportPath = "build/reports/jacoco/test/jacocoTestReport.xml"
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
