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
    private List<SocketWithUrl> sockets;
    private String name;
    private String url;

    public JsonWebSocketHandler(String name, String url) {
        this.sockets = new ArrayList<>();
        this.name = name;
        this.url = url;
    }

    public abstract void onConnect(String uri);

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void handle(ServerWebSocket ws) {
        if (! ws.uri().startsWith(url)) {
            return;
        }

        sockets.add(new SocketWithUrl(ws, ws.uri()));

        ConsoleOutputs.out(name + " connected: ", BLUE, ws.path());
        renderNumberOfSockets();

        onConnect(ws.uri());

        ws.handler(data -> {
            String dataString = data.getString(0, data.length());
            System.out.println(dataString);
        });

        ws.closeHandler((h) -> {
            sockets.removeIf(s -> s.socket == ws);
            ConsoleOutputs.out(RED, name + " connection closed");
            renderNumberOfSockets();
        });
    }

    public void send(String subUrlToContain, Map<String, ?> payload) {
        if (sockets.isEmpty()) {
            ConsoleOutputs.out(BLUE, name + " connection ", FontStyle.NORMAL, "with", BLUE, " web page ", FontStyle.NORMAL, "is not established. ",
                    BLUE, "reload or open", FontStyle.NORMAL, " the page");
            return;
        }

        String text = JsonUtils.serialize(payload);
        ConsoleOutputs.out(name + " sending: ", BLUE, text);

        sockets.stream()
                .filter(s -> s.connectedUrl.contains(subUrlToContain))
                .forEach(s -> s.socket.writeFinalTextFrame(text));
    }

    private void renderNumberOfSockets() {
        ConsoleOutputs.out("there are " + sockets.size() + " opened sockets for " + name);
    }

    private static class SocketWithUrl {
        private final ServerWebSocket socket;
        private final String connectedUrl;

        public SocketWithUrl(ServerWebSocket socket, String connectedUrl) {
            this.socket = socket;
            this.connectedUrl = connectedUrl;
        }
    }
}
