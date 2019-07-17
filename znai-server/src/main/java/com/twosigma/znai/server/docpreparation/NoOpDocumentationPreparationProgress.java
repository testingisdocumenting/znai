package com.twosigma.znai.server.docpreparation;

import java.util.Map;

public class NoOpDocumentationPreparationProgress implements DocumentationPreparationProgress {
    public static final DocumentationPreparationProgress INSTANCE = new NoOpDocumentationPreparationProgress();

    @Override
    public void reportProgress(String message, Map<String, String> keyValues, int progressPercent) {
    }
}
