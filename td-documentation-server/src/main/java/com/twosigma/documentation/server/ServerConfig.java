package com.twosigma.documentation.server;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class ServerConfig {
    private Path rootOfDocs;

    ServerConfig(String... args) {
        if (args.length < 1) {
            throw new RuntimeException("required argument <root-of-docs> is required");
        }

        rootOfDocs = Paths.get(args[0]).toAbsolutePath();
    }

    public Path getRootOfDocs() {
        return rootOfDocs;
    }

    @Override
    public String toString() {
        return "rootOfDocs: " + rootOfDocs + "\n";
    }
}
