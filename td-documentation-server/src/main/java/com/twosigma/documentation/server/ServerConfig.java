package com.twosigma.documentation.server;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class ServerConfig {
    private Path deployRoot;
    private Path docRoot;

    public ServerConfig(String... args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        if (args.length >= 1) {
            docRoot = Paths.get(args[0]).toAbsolutePath();
        }
    }

    public Path getDocRoot() {
        return validateIsSet("docRoot", docRoot);
    }

    public void setDocRoot(Path docRoot) {
        this.docRoot = docRoot;
    }

    public void setDeployRoot(Path deployRoot) {
        this.deployRoot = deployRoot;
    }

    public Path getDeployRoot() {
        return validateIsSet("deployRoot", deployRoot);
    }

    private <E> E validateIsSet(String name, E v) {
        if (v == null) {
            throw new RuntimeException("<" + name + "> is not set. specify it using args or a configuration set method");
        }

        return v;
    }

    @Override
    public String toString() {
        return "deployRoot: " + deployRoot + "\n";
    }
}
