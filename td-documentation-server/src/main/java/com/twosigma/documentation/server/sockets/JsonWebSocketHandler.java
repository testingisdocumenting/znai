package com.twosigma.documentation.server.sockets;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.FontStyle;
import com.twosigma.utils.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.twosigma.console.ansi.Color.BLUE;
import static com.twosigma.console.ansi.Color.RED;

public abstract class JsonWebSocketHandler implements Handler<ServerWebSocket> {
    private List<ServerWebSocket> sockets;
    private String id;
    private String url;

    public JsonWebSocketHandler(String id, String url) {
        this.sockets = new ArrayList<>();
        this.id = id;
        this.url = url;
    }

    public abstract void onConnect(String uri);

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void handle(ServerWebSocket ws) {
        if (! ws.uri().startsWith(url)) {
            return;
        }

        sockets.add(ws);

        ConsoleOutputs.out(id + " connected: ", BLUE, ws.path());
        renderNumberOfSockets();

        onConnect(ws.uri());

        ws.handler(data -> {
            String dataString = data.getString(0, data.length());
            System.out.println(dataString);
        });

        ws.closeHandler((h) -> {
            sockets.remove(ws);
            ConsoleOutputs.out(RED, id + " connection closed");
            renderNumberOfSockets();
        });

    }

    public void send(Map<String, ?> payload) {
        if (sockets.isEmpty()) {
            ConsoleOutputs.out(BLUE, id + " connection ", FontStyle.NORMAL, "with", BLUE, " web page ", FontStyle.NORMAL, "is not established. ",
                    BLUE, "reload or open", FontStyle.NORMAL, " the page");
            return;
        }

        String text = JsonUtils.serialize(payload);
        ConsoleOutputs.out(id + " sending: ", BLUE, text);
        sockets.forEach(ws -> ws.writeFinalTextFrame(text));
    }

    private void renderNumberOfSockets() {
        ConsoleOutputs.out("there are " + sockets.size() + " opened sockets for " + id);
    }
}
