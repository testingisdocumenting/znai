package com.twosigma.documentation.server.docpreparation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class DocumentationPreparationTestHandler implements DocumentationPreparationHandler {
    private static final String DUMMY_DOC_ID = "preview";

    private AtomicBoolean isReady = new AtomicBoolean(true);

    @Override
    public boolean handles(String docId) {
        return docId.equals(DUMMY_DOC_ID);
    }

    @Override
    public boolean isReady(String docId) {
        return docId.equals("preview") && this.isReady.get();
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
            this.isReady.set(true);
            preparationProgress.reportProgress("another progress", keyValues, 100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
