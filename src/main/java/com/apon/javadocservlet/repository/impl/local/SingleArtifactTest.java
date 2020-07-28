package com.apon.javadocservlet.repository.impl.local;

import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.ArtifactVersions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SingleArtifactTest implements ArtifactStorage {
    private final static Logger log = LogManager.getLogger(SingleArtifactTest.class);

    private final static String FILE_LOCATION = "streamex-0.7.2-javadoc.jar";
    private final static String GROUP_ID = "one.util";
    private final static String ARTIFACT_ID = "streamex";
    private final static String VERSION = "0.7.2";
    private final static Artifact ARTIFACT = new Artifact(GROUP_ID, ARTIFACT_ID, VERSION);
    private final static ArtifactVersions ARTIFACT_VERSIONS = new ArtifactVersions(ARTIFACT, Collections.singletonList(new ArtifactVersions.Version(VERSION, true)));

    @Override
    public List<Artifact> findArtifacts(String groupId, String artifactId) {
        List<Artifact> artifacts = new ArrayList<>();

        if (GROUP_ID.equals(groupId) && ARTIFACT_ID.equals(artifactId)) {
            artifacts.add(ARTIFACT);
        }

        return artifacts;
    }

    @Override
    public ArtifactVersions findArtifactVersions(Artifact artifact) {
        if (ARTIFACT.equals(artifact)) {
            return ARTIFACT_VERSIONS;
        }
        return null;
    }

    @Override
    public byte[] getJavaDocJar(String groupId, String artifactId, String version) throws ArtifactSearchException {
        log.debug("Reading jar from resources.");

        if (!GROUP_ID.equals(groupId) || !ARTIFACT_ID.equals(artifactId) || !VERSION.equals(version)) {
            throw new ArtifactSearchException("Cannot find this artifact.");
        }

        try (InputStream inputStream = SingleArtifactTest.class.getClassLoader().getResourceAsStream(FILE_LOCATION)) {
            if (inputStream == null) {
                throw new ArtifactSearchException("Could not find artifact.");
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new ArtifactSearchException(e);
        }
    }
}
