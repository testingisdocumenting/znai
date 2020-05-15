/*
 * Copyright 2020 znai maintainers
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
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import org.testingisdocumenting.znai.utils.ServiceLoaderUtils;

import java.util.Set;
import java.util.stream.Stream;

public class WebSocketHandlers {
    private static final Set<WebSocketHandler> handlers =
            ServiceLoaderUtils.load(WebSocketHandler.class);

    public static final Handler<ServerWebSocket> handler = WebSocketHandlers::handle;

    public static void add(WebSocketHandler provider) {
        handlers.add(provider);
    }

    public static void remove(WebSocketHandler provider) {
        handlers.remove(provider);
    }

    public static void init(Vertx vertx) {
        handlers.forEach(h -> h.init(vertx));
    }

    private static void handle(ServerWebSocket ws) {
        WebSocketHandler socketHandler = handlers.stream()
                .filter(h -> h.handles(ws))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no socket handler registered for " + ws.uri()));

        socketHandler.handle(ws);
    }
}
