[![Build Status](https://travis-ci.com/GreenT13/javadoc-web-application.svg?branch=master)](https://travis-ci.com/GreenT13/javadoc-web-application)
[![Coverage Status](https://coveralls.io/repos/github/GreenT13/javadoc-web-application/badge.svg?branch=master)](https://coveralls.io/github/GreenT13/javadoc-web-application?branch=master)

# Javadoc web application
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

# TODO items
- [ ] Remove all TODO's from the code

## Tests
- [ ] Integration tests with mocked Maven Central
- [ ] Thymeleaf tests?? https://dzone.com/articles/spring-test-thymeleaf-views
- [x] UrlUtil where ServletContext return some root path.
- [ ] Run through all tests, cleanup code and make some util functions for easily creating objects
- [ ] Replace Mockito.mock with actual classes to speed up performance?
- [x] Add specific cache control test with checking headers and 304 response.
- [x] Add integration test for the complete cache strategy

## General code thoughts
- [ ] Error handling for incorrect URLs
- [ ] Rethink exception catching strategy in ApiDocController
- [ ] Support for "latest" URLs
- [ ] Add @NonNull and @Nullable annotation throughout the code
- [x] Add caching for ApiDocController methods (https://www.baeldung.com/spring-mvc-cache-headers)

## Random technical stuff
- [ ] Add IntelliJ run configurations

## Integrate repositories
Create a single ENUM with all repository storages. I think we should maybe integrate them in some way, but what
if you have a single artifact in multiple repositories?

For testing, it is nice to select multiple, but for actual usage this would probably not be needed. Maybe it should
be a system configuration which repository you select?
