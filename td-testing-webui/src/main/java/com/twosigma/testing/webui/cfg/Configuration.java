package com.twosigma.testing.webui.cfg;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class Configuration {
    public static final Configuration INSTANCE = new Configuration();

    private String baseUrl;

    private Configuration() {
        initBaseUrl();
    }

    public Path getDocumentationArtifactsPath() {
        return Paths.get("/Users/mykola/work/testing-documenting/td-documentation/documentation/synergy-with-testing");
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private void initBaseUrl() {
        baseUrl = configValue("url");
    }

    private String configValue(String key) {
        String value = System.getProperty(key);
        return value;
    }
}
