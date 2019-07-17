package com.twosigma.znai.cli.extension;

import java.nio.file.Path;

public class CliCommandConfig {
    private String docId;
    private Path sourceRoot;
    private Path deployRoot;

    public CliCommandConfig(String docId, Path sourceRoot, Path deployRoot) {
        this.docId = docId;
        this.sourceRoot = sourceRoot;
        this.deployRoot = deployRoot;
    }

    public String getDocId() {
        return docId;
    }

    public Path getSourceRoot() {
        return sourceRoot;
    }

    public Path getDeployRoot() {
        return deployRoot;
    }

    @Override
    public String toString() {
        return "CliCommandConfig{" +
                "docId='" + docId + '\'' +
                ", sourceRoot=" + sourceRoot +
                ", deployRoot=" + deployRoot +
                '}';
    }
}
