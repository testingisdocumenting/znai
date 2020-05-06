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

package org.testingisdocumenting.znai.server.sockets;

import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.ArrayList;
import java.util.List;

public class JsonWebSocketHandlerComposition implements Handler<ServerWebSocket> {
    private List<JsonWebSocketHandler> handlers;

    public JsonWebSocketHandlerComposition() {
        this.handlers = new ArrayList<>();
    }

    public void add(JsonWebSocketHandler socketHandler) {
        handlers.add(socketHandler);
    }

    @Override
    public void handle(ServerWebSocket ws) {
        JsonWebSocketHandler socketHandler = handlers.stream()
                .filter(h -> ws.uri().startsWith(h.getUrl()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no socket handler registered for " + ws.uri()));

        socketHandler.handle(ws);
    }
}
