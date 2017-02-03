package com.twosigma.documentation.client;

/**
 * @author mykola
 */
public class ClientConfig {
    private String docId;

    public ClientConfig(String[] args) {
        parseConfig(args);
    }

    private void parseConfig(String[] args) {
        this.docId = args[0];
    }

    public String getDocId() {
        return docId;
    }
}
