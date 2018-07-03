package com.twosigma.documentation.server.docpreparation;

import io.vertx.core.impl.ConcurrentHashSet;

import java.util.*;

public class DocumentationPreparationTestHandler implements DocumentationPreparationHandler {
    private static final List<String> DUMMY_DOC_IDS = Arrays.asList("preview", "hello", "world");

    private final Set<String> isReadyById = new ConcurrentHashSet<>();

    @Override
    public boolean handles(String docId) {
        return DUMMY_DOC_IDS.contains(docId);
    }

    @Override
    public boolean isReady(String docId) {
        return isReadyById.contains(docId);
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
            isReadyById.add(docId);
            preparationProgress.reportProgress("another progress", keyValues, 100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
