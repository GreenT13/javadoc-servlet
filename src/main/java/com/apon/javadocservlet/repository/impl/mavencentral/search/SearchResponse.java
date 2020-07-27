package com.apon.javadocservlet.repository.impl.mavencentral.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse {
    public Response response;

    public static class Response {
        public int numFound;
        public int start;
        public List<Doc> docs;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Doc {
        @JsonProperty("g")
        public String groupId;
        @JsonProperty("a")
        public String artifactId;
        public String latestVersion;
        public List<String> ec;
    }
}
