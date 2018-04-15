package com.twosigma.documentation.server.docpreparation;

public interface DocumentationPreparationHandler {
    boolean handles(String docId);
    void prepare(String docId, DocumentationPreparationProgress preparationProgress);
}
