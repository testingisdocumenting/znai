package com.twosigma.documentation.server.docpreparation;

import com.twosigma.documentation.server.sockets.JsonWebSocketHandler;
import com.twosigma.utils.CollectionUtils;
import io.vertx.core.Vertx;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class DocumentationPreparationWebSocketHandler extends JsonWebSocketHandler {
    private Vertx vertx;

    public DocumentationPreparationWebSocketHandler(Vertx vertx) {
        super("documentation update", "/_doc-update");
        this.vertx = vertx;
    }

    public void sendUpdate(String message, Map<String, String> keyValues, int progressPercent) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("message", message);
        payload.put("progress", progressPercent);

        List<Map<Object, Object>> keyValuesList = keyValues.keySet().stream()
                .map(key -> CollectionUtils.createMap("key", key, "value", keyValues.get(key)))
                .collect(Collectors.toList());
        payload.put("keyValues", keyValuesList);

        send(payload);
    }

    @Override
    public void onConnect(String uri) {
        int lastSepIdx = uri.lastIndexOf('/');
        String docId = uri.substring(lastSepIdx + 1);

        vertx.executeBlocking((req) -> {
            DocumentationPreparationHandlers.prepare(docId, new DocumentationPreparationSocketProgress(this));
        }, (res) -> {

        });
    }
}
