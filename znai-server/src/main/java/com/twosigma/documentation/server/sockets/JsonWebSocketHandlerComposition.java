package com.twosigma.documentation.server.sockets;

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
