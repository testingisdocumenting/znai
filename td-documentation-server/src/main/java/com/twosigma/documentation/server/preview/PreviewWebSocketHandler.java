package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.FontStyle;
import com.twosigma.documentation.html.PageProps;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.utils.JsonUtils;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;

import java.util.ArrayList;
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
    private List<ServerWebSocket> sockets = new ArrayList<>();

    @Override
    public void handle(ServerWebSocket ws) {
        sockets.add(ws);

        ConsoleOutputs.out("connected: ", BLUE, ws.path());
        renderNumberOfSockets();

        ws.handler(data -> {
            String dataString = data.getString(0, data.length());
            System.out.println(dataString);
        });

        ws.closeHandler((h) -> {
            sockets.remove(ws);
            ConsoleOutputs.out(RED, "connection closed");
            renderNumberOfSockets();
        });
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

    public void sendToc(TableOfContents newToc) {
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "tocUpdate");
        payload.put("toc", newToc.toListOfMaps());

        send(payload);
    }

    public void sendMeta(DocMeta docMeta) {
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "docMetaUpdate");
        payload.put("docMeta", docMeta.toMap());

        send(payload);
    }

    public void sendError(String error) {
        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "error");
        payload.put("error", error);

        send(payload);
    }

    private void send(Map<String, ?> payload) {
        if (sockets.isEmpty()) {
            ConsoleOutputs.out(BLUE, "connection ", FontStyle.NORMAL, "with", BLUE, " web page ", FontStyle.NORMAL, "is not established. ",
                    BLUE, "reload or open", FontStyle.NORMAL, " the page");
            return;
        }

        String text = JsonUtils.serialize(payload);
        ConsoleOutputs.out("sending: ", BLUE, text);
        sockets.forEach(ws -> ws.writeFinalTextFrame(text));
    }

    private void renderNumberOfSockets() {
        ConsoleOutputs.out("there are " + sockets.size() + " opened sockets");
    }
}
