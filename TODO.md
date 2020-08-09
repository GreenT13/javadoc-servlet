## Tests
Other possible tests:
- [ ] Integration tests with mocked Maven Central
- [ ] Thymeleaf tests?? https://dzone.com/articles/spring-test-thymeleaf-views
- [ ] UrlUtil where ServletContext return some root path.
- [ ] Run through all tests and cleanup code

## General thoughts
- [x] Display kind of error message when version does not support JavaDoc
- [ ] Error handling for incorrect URLs
- [ ] Support for "latest" URLs

## Integrate repositories
Create a single ENUM with all repository storages. I think we should maybe integrate them in some way, but what
if you have a single artifact in multiple repositories?

For testing, it is nice to select multiple, but for actual usage this would probably not be needed. Maybe it should
be a system configuration which repository you select?

## Random technical stuff
- [x] Travis-ci + badge
- [x] JaCoCo plugin + Coveralls + badge
- [x] Checkstyle (fail build if plugin fails)
- [ ] SpotBugs
- [ ] Convert Gradle to Kotlin variant
- [ ] Add IntelliJ run configurations
