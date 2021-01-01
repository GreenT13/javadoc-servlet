## Javadoc web application
[![Build Status](https://travis-ci.com/ricoapon/javadoc-web-application.svg?branch=master)](https://travis-ci.com/ricoapon/javadoc-web-application)
[![codecov](https://codecov.io/gh/ricoapon/javadoc-web-application/branch/master/graph/badge.svg)](https://codecov.io/gh/ricoapon/javadoc-web-application)
[![Docs](https://img.shields.io/badge/docs-Github%20Pages-blue)](https://greent13.github.io/javadoc-web-application/)
[![Deployment](https://img.shields.io/badge/deployment-Heroku-brightgreen)](https://javadoc-web-application.herokuapp.com/)

Java web application for displaying javadoc.jar artifacts in the browser! It downloads the artifacts automatically
from repositories, based on given search criteria. Uses a lot of caching to avoid too many requests.

# TODO items

## Tests
- [x] Thymeleaf tests (https://dzone.com/articles/spring-test-thymeleaf-views)
- [ ] Integration tests with mocked Maven Central
- [ ] Run through all tests, cleanup code and make some util functions for easily creating objects

## General code thoughts
- [x] Error handling for incorrect URLs
- [x] Rethink exception catching strategy in ApiDocController
- [ ] Support for "latest" URLs
- [ ] Add a "share" button, where you can create a link with specific details (link to class, package, home URL without index.html)
      https://stackoverflow.com/questions/23466130/spring-mvc-how-do-i-get-current-url-in-thymeleaf
      https://stackoverflow.com/questions/2222238/httpservletrequest-to-complete-url
- [ ] Add more to the docs
- [ ] Rethink Artifact/ArtifactVersion/ArtifactVersion.Version object structure
