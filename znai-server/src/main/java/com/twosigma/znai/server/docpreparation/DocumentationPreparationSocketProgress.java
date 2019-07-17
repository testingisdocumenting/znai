package com.twosigma.znai.server.docpreparation;

import java.util.Map;

public class DocumentationPreparationSocketProgress implements DocumentationPreparationProgress {
    private final String docId;
    private DocumentationPreparationWebSocketHandler socketHandler;

    public DocumentationPreparationSocketProgress(String docId, DocumentationPreparationWebSocketHandler socketHandler) {
        this.docId = docId;
        this.socketHandler = socketHandler;
    }

    @Override
    public void reportProgress(String message, Map<String, String> keyValues, int progressPercent) {
        socketHandler.sendUpdate(docId, message, keyValues, progressPercent);
    }
}
