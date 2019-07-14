package com.twosigma.documentation.server.preview;

import com.twosigma.documentation.html.DocPageReactProps;
import com.twosigma.documentation.server.sockets.JsonWebSocketHandler;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class PreviewWebSocketHandler extends JsonWebSocketHandler {
    private static final String NAME = "preview";
    private static final String URL = "/preview";

    PreviewWebSocketHandler() {
        super(NAME, URL);
    }

    public void sendPage(DocPageReactProps pageProps) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "pageUpdate");
        payload.put("pageProps", pageProps.toMap());

        send(payload);
    }

    public void sendPages(Stream<DocPageReactProps> listOfPageProps) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "multiplePagesUpdate");
        payload.put("listOfPageProps", listOfPageProps.map(DocPageReactProps::toMap).collect(toList()));

        send(payload);
    }

    public void sendToc(TableOfContents newToc) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "tocUpdate");
        payload.put("toc", newToc.toListOfMaps());

        send(payload);
    }

    public void sendMeta(DocMeta docMeta) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "docMetaUpdate");
        payload.put("docMeta", docMeta.toMap());

        send(payload);
    }

    public void sendError(String error) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "error");
        payload.put("error", error);

        send(payload);
    }

    private void send(Map<String, ?> payload) {
        send(URL, payload);
    }

    @Override
    public void onConnect(String uri) {
    }
}
