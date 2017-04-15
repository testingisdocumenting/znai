package com.twosigma.testing.webui.cfg;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class Configuration {
    public static final Configuration INSTANCE = new Configuration();

    public Path getDocumentationArtifactsPath() {
        return Paths.get("/Users/mykola/work/testing-documenting/td-documentation/documentation/synergy-with-testing");
    }
}
