package com.apon.javadocservlet.repository.impl.mavencentral;

import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.ArtifactVersions;
import com.apon.javadocservlet.repository.impl.mavencentral.search.SearchResponse;
import com.apon.javadocservlet.repository.impl.mavencentral.search.VersionsSearchResponse;
import com.apon.javadocservlet.repository.impl.WebserviceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class MavenCentralRepository implements ArtifactStorage {
    private final static Logger log = LogManager.getLogger(MavenCentralRepository.class);

    private final MavenCentralApi mavenCentralApi;

    public MavenCentralRepository(WebserviceClient webserviceClient) {
        this.mavenCentralApi = new MavenCentralApi(webserviceClient);
    }

    @Override
    public List<Artifact> findArtifacts(String groupId, String artifactId) throws ArtifactSearchException {
        SearchResponse searchResponse = mavenCentralApi.findByGroupIdAndArtifactId(groupId, artifactId);
        return convertSearchResponseToArtifactList(searchResponse);
    }

    @Override
    public ArtifactVersions findArtifactVersions(Artifact artifact) throws ArtifactSearchException {
        VersionsSearchResponse versionsSearchResponse = mavenCentralApi.findVersions(artifact.getGroupId(), artifact.getArtifactId());
        return convertSearchResponseToArtifactVersions(versionsSearchResponse, artifact);
    }

    @Override
    public byte[] getJavaDocJar(String groupId, String artifactId, String version) {
        log.debug("Retrieving javadoc jar");
        return mavenCentralApi.getJavaDocJar(groupId, artifactId, version);
    }

    private List<Artifact> convertSearchResponseToArtifactList(SearchResponse searchResponse) {
        return searchResponse.response.docs.stream()
                .map((doc) -> new Artifact(doc.groupId, doc.artifactId, doc.latestVersion))
                .collect(Collectors.toList());
    }

    private ArtifactVersions convertSearchResponseToArtifactVersions(VersionsSearchResponse versionsSearchResponse, Artifact artifact) {
        List<ArtifactVersions.Version> versions = versionsSearchResponse.response.docs.stream()
                .map((doc) -> new ArtifactVersions.Version(doc.version, doc.ec.contains("-javadoc.jar")))
                .collect(Collectors.toList());

        return new ArtifactVersions(artifact, versions);
    }
}
