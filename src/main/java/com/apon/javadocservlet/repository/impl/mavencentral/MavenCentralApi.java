package com.apon.javadocservlet.repository.impl.mavencentral;

import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.impl.mavencentral.search.SearchResponse;
import com.apon.javadocservlet.repository.impl.mavencentral.search.VersionsSearchResponse;
import com.apon.javadocservlet.repository.impl.WebserviceClient;

import java.util.Map;

public class MavenCentralApi {
    private final static String MAVEN_CENTRAL_SEARCH_URL = "https://search.maven.org/solrsearch/select";
    private final static String MAVEN_CENTRAL_FILEPATH_URL = "https://search.maven.org/remotecontent";

    private final WebserviceClient webserviceClient;

    public MavenCentralApi(WebserviceClient webserviceClient) {
        this.webserviceClient = webserviceClient;
    }

    public SearchResponse findByGroupIdAndArtifactId(String groupId, String artifactId) throws ArtifactSearchException {
        if (groupId == null && artifactId == null) {
            throw new ArtifactSearchException("You have to fill in at least the groupId or the artifactId.");
        }

        StringBuilder searchQuery = new StringBuilder();
        if (groupId != null) {
            searchQuery.append("g:\"").append(groupId).append("\"");

            if (artifactId != null) {
                searchQuery.append(" AND ");
            }
        }

        if (artifactId != null) {
            searchQuery.append("a:\"").append(artifactId).append("\"");
        }

        return webserviceClient.get(MAVEN_CENTRAL_SEARCH_URL, Map.of("q", searchQuery.toString()), SearchResponse.class);
    }

    public VersionsSearchResponse findVersions(String groupId, String artifactId) throws ArtifactSearchException {
        if (groupId == null || artifactId == null) {
            throw new ArtifactSearchException("You have to fill in both groupId and artifactId.");
        }

        String searchQuery = "g:\"" + groupId + "\"" + " AND " + "a:\"" + artifactId + "\"";

        return webserviceClient.get(MAVEN_CENTRAL_SEARCH_URL, Map.of("q", searchQuery, "core", "gav"), VersionsSearchResponse.class);
    }

    public byte[] getJavaDocJar(String groupId, String artifactId, String version) {
        String filePath = groupId.replace('.', '/') + '/' + artifactId + '/' + version + '/' + artifactId + "-" + version + "-javadoc.jar";
        return webserviceClient.getJar(MAVEN_CENTRAL_FILEPATH_URL, Map.of("filepath", filePath));
    }
}
