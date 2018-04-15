package com.twosigma.documentation.server.docpreparation;

import java.util.Map;

public class DocumentationPreparationSocketProgress implements DocumentationPreparationProgress {
    private DocumentationPreparationWebSocketHandler socketHandler;

    public DocumentationPreparationSocketProgress(DocumentationPreparationWebSocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void reportProgress(String message, Map<String, String> keyValues, int progressPercent) {
        socketHandler.sendUpdate(message, keyValues, progressPercent);
    }
}
