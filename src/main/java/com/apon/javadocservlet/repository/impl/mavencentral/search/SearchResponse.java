package com.apon.javadocservlet.repository.impl.mavencentral.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings(justification = "This class is filled dynamically (Jackson), which is not seen by SpotBugs.")
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
