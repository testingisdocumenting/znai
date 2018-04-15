package com.twosigma.documentation.server.docpreparation;

import io.vertx.core.Vertx;

import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentationPreparationTestHandler implements DocumentationPreparationHandler {
    @Override
    public boolean handles(String docId) {
        return docId.equals("preview");
    }

    @Override
    public void prepare(String docId, DocumentationPreparationProgress preparationProgress) {
        Map<String, String> keyValues = new LinkedHashMap<>();

        try {
            Thread.sleep(1000);
            preparationProgress.reportProgress("hello", keyValues, 15);

            Thread.sleep(1000);
            keyValues.put("code base", "test_codebase");
            preparationProgress.reportProgress("world", keyValues,45);

            Thread.sleep(2000);
            keyValues.put("branch", "branch name");
            preparationProgress.reportProgress("some progress", keyValues, 75);

            Thread.sleep(1000);
            preparationProgress.reportProgress("another progress", keyValues, 100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
