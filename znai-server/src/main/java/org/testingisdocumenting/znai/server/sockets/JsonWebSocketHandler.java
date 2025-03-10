/*
 * Copyright 2020 znai maintainers
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

import io.vertx.core.Vertx;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.FontStyle;
import org.testingisdocumenting.znai.utils.JsonUtils;
import io.vertx.core.http.ServerWebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.znai.console.ansi.Color.BLUE;
import static org.testingisdocumenting.znai.console.ansi.Color.RED;

public abstract class JsonWebSocketHandler implements WebSocketHandler {
    private final List<SocketWithUrl> sockets;
    private final String name;
    private final String url;
    protected Vertx vertx;

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
    public void init(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public boolean handles(ServerWebSocket ws) {
        return ws.uri().startsWith(url);
    }

    @Override
    public void handle(ServerWebSocket ws) {
        if (!handles(ws)) {
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
            System.out.println("#### empty");
            // TODO how to make it not print
//            ConsoleOutputs.out(BLUE, name + " connection ", FontStyle.NORMAL, "with", BLUE, " web page ", FontStyle.NORMAL, "is not established. ",
//                    BLUE, "reload or open", FontStyle.NORMAL, " the page");
            return;
        }

        Object typeVal = payload.get("type");
        String type = typeVal != null ? typeVal.toString() : "";
        String payloadAsText = JsonUtils.serialize(payload);
//        ConsoleOutputs.out(name + " sending: ", BLUE, type);

        sockets.stream()
                .filter(s -> s.connectedUrl.contains(subUrlToContain))
                .forEach(s -> s.socket.writeFinalTextFrame(payloadAsText));
    }

    private void renderNumberOfSockets() {
        ConsoleOutputs.out("there are " + sockets.size() + " opened sockets for " + name);
    }

    private record SocketWithUrl(ServerWebSocket socket, String connectedUrl) {
    }
}
