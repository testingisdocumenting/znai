package com.twosigma.testing.webui.cfg;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class Configuration {
    public static final Configuration INSTANCE = new Configuration();

    private String baseUrl;
    private Path docArtifactsPath;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Path getDocArtifactsPath() {
        return docArtifactsPath;
    }

    public void setDocArtifactsPath(Path docArtifactsPath) {
        this.docArtifactsPath = docArtifactsPath;
    }
}
