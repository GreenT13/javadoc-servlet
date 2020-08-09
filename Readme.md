[![Build Status](https://travis-ci.org/GreenT13/javadoc-servlet.svg?branch=master)](https://travis-ci.org/GreenT13/javadoc-servlet)
[![Coverage Status](https://coveralls.io/repos/github/GreenT13/javadoc-servlet/badge.svg?branch=master)](https://coveralls.io/github/GreenT13/javadoc-servlet?branch=master)

# Javadoc servlet
Java web application for displaying javadoc.jar artifacts in the browser! It downloads the artifacts automatically
from repositories, based on given search criteria. Uses a lot of caching to avoid too many requests.

## Technology stack
* Java 11
* Gradle (Kotlin variant)
* Spring Boot
* Thymeleaf templates (easier to setup than SPA)

I use the following CICD tools (see badges for the links):
* Travis-ci.org
* Coveralls.io

I use the following tools for checking code quality:
* Checkstyle
* SpotBugs
