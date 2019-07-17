package com.twosigma.znai.server.docpreparation;

import com.twosigma.znai.server.sockets.JsonWebSocketHandler;
import com.twosigma.utils.CollectionUtils;
import io.vertx.core.Vertx;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocumentationPreparationWebSocketHandler extends JsonWebSocketHandler {
    private Vertx vertx;

    public DocumentationPreparationWebSocketHandler(Vertx vertx) {
        super("documentation update", "/_doc-update");
        this.vertx = vertx;
    }

    public void sendUpdate(String docId, String message, Map<String, String> keyValues, int progressPercent) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("message", message);
        payload.put("progress", progressPercent);

        List<Map<Object, Object>> keyValuesList = keyValues.keySet().stream()
                .map(key -> CollectionUtils.createMap("key", key, "value", keyValues.get(key)))
                .collect(Collectors.toList());
        payload.put("keyValues", keyValuesList);

        send("/" + docId, payload);
    }

    @Override
    public void onConnect(String uri) {
        int lastSepIdx = uri.lastIndexOf('/');
        String docId = uri.substring(lastSepIdx + 1);

        vertx.executeBlocking((future) -> DocumentationPreparationHandlers.prepare(docId,
                new DocumentationPreparationSocketProgress(docId, this)),
                false,
                (res) -> { });
    }
}
