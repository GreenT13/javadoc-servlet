## Tests
Packages to write tests for:
- [x] controllers
- [x] repository
- [ ] client (probably not doable to write tests for this one?)
- [x] zip

Other possible tests:
- [ ] Integration tests with mocked Maven Central
- [ ] Thymeleaf tests?? https://dzone.com/articles/spring-test-thymeleaf-views
- [ ] UrlUtil where ServletContext return some root path.

## UI enhancements
- IFrame page:
  - [x] Make iframe fill page
  - [x] Make header above iframe with version selection
- Home page:
  - [ ] Add links in result table to the javadoc if it is available
  - [ ] Make it possible to select specific versions? Something like a button "Show versions"
        or something?
  - [ ] Some CSS and make it a nice looking page

## General thoughts
- [ ] Display kind of error message when version does not support JavaDoc
- [ ] Error handling for incorrect URLs
- [ ] Support for "latest" URLs
- [x] Make it possible to run application on non-root URL

## Integrate repositories
Create a single ENUM with all repository storages. I think we should maybe integrate them in some way, but what
if you have a single artifact in multiple repositories?

For testing, it is nice to select multiple, but for actual usage this would probably not be needed. Maybe it should
be a system configuration which repository you select?
