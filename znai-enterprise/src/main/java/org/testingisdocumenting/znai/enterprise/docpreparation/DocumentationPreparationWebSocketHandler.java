/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.enterprise.docpreparation;

import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationHandlers;
import org.testingisdocumenting.znai.server.sockets.JsonWebSocketHandler;
import org.testingisdocumenting.znai.utils.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocumentationPreparationWebSocketHandler extends JsonWebSocketHandler {
    public DocumentationPreparationWebSocketHandler() {
        super("documentation update", "/_doc-update");
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
