package com.twosigma.documentation.server;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class ServerConfig {
    private Path rootOfDocs;

    public ServerConfig(String... args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        if (args.length >= 1) {
            rootOfDocs = Paths.get(args[0]).toAbsolutePath();
        }
    }

    public void setRootOfDocs(Path rootOfDocs) {
        this.rootOfDocs = rootOfDocs;
    }

    public Path getRootOfDocs() {
        if (rootOfDocs == null) {
            throw new RuntimeException("<rootOfDocs> is not set. specify it using args or a configuration set method");
        }
        return rootOfDocs;
    }

    @Override
    public String toString() {
        return "rootOfDocs: " + rootOfDocs + "\n";
    }
}
