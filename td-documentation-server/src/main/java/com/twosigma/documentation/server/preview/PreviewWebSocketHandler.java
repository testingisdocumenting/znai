package com.twosigma.documentation.server.preview;

import com.twosigma.utils.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.Collections;
import java.util.Map;

/**
 * @author mykola
 */
public class PreviewWebSocketHandler implements Handler<ServerWebSocket> {
    private ServerWebSocket ws;

    @Override
    public void handle(ServerWebSocket ws) {
        this.ws = ws;
        System.out.println("connected: " + ws.path());

        ws.handler(data -> {
            String dataString = data.getString(0, data.length());
            System.out.println(dataString);
        });

        sendJson(Collections.singletonMap("type", "reload"));
    }

    public void sendJson(Map<String, ?> json) {
        String text = JsonUtils.serialize(json);
        ws.writeFinalTextFrame(text);
    }
}
