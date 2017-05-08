package com.twosigma.testing.webui.cfg;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class WebUiTestConfig {
    public static final WebUiTestConfig INSTANCE = new WebUiTestConfig();

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
