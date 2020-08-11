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
- [ ] UrlUtil where ServletContext return some root path.
- [ ] Run through all tests, cleanup code and make some util functions for easily creating objects
- [ ] Replace Mockito.mock with actual classes to speed up performance?
- [x] Add specific cache control test with checking headers and 304 response.
- [ ] Add integration test for the complete cache strategy:
      1. When sending the first request (index.html), the file will be retrieved once from artifact storage.
      2. All files that are loaded afterwards, are retrieved from the already processed zip in cache.
      3. All requests have the etag-header with the md5 hash of the zip.
      4. When sending request with If-None-Match and correct identifier, return 304 response.
      5. When sending request with If-None-Match and incorrect identifier, retrieve file from cache.
      6. In all the above, we call the artifact storage exactly once.

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
