package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutput;
import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.console.ansi.FontStyle;
import com.twosigma.documentation.html.PageProps;
import com.twosigma.utils.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.twosigma.console.ansi.Color.BLUE;
import static com.twosigma.console.ansi.Color.RED;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class PreviewWebSocketHandler implements Handler<ServerWebSocket> {
    private ServerWebSocket ws;

    @Override
    public void handle(ServerWebSocket ws) {
        this.ws = ws;
        ConsoleOutputs.out("connected: ", BLUE, ws.path());

        ws.handler(data -> {
            String dataString = data.getString(0, data.length());
            System.out.println(dataString);
        });

        ws.closeHandler((h) -> ConsoleOutputs.out(RED, "connection closed"));
    }

    public void sendPage(PageProps pageProps) {
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "pageUpdate");
        payload.put("pageProps", pageProps.toMap());

        send(payload);
    }

    public void sendPages(Stream<PageProps> listOfPageProps) {
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "multiplePagesUpdate");
        payload.put("listOfPageProps", listOfPageProps.map(PageProps::toMap).collect(toList()));

        send(payload);
    }

    private void send(Map<String, ?> payload) {
        if (ws == null) {
            ConsoleOutputs.out(BLUE, "connection ", FontStyle.NORMAL, "with", BLUE, " web page ", FontStyle.NORMAL, "is not established. ",
                    BLUE, "reload or open", FontStyle.NORMAL, " the page");
            return;
        }



        String text = JsonUtils.serialize(payload);
        ConsoleOutputs.out("sending: ", BLUE, text);
        ws.writeFinalTextFrame(text);
    }
}
