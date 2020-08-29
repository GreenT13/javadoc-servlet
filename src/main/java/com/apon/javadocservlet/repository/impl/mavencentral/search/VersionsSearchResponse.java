package com.apon.javadocservlet.repository.impl.mavencentral.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings(justification = "This class is filled dynamically (Jackson), which is not seen by SpotBugs.")
public class VersionsSearchResponse {
    @JsonProperty("response")
    public Response response;

    public static class Response {
        @JsonProperty("docs")
        public List<Doc> docs;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Doc {
        @JsonProperty("g")
        public String groupId;
        @JsonProperty("a")
        public String artifactId;
        @JsonProperty("v")
        public String version;
        @JsonProperty("ec")
        public List<String> ec;
    }
}
