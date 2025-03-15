/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.server.preview;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.FontStyle;
import org.testingisdocumenting.znai.html.DocPageReactProps;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.server.sockets.JsonWebSocketHandler;
import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.structure.Footer;
import org.testingisdocumenting.znai.structure.TableOfContents;
import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.testingisdocumenting.znai.console.ansi.Color.BLUE;

public class PreviewSendChangesWebSocketHandler extends JsonWebSocketHandler {
    private static final String NAME = "preview";
    private static final String URL = "/preview";

    PreviewSendChangesWebSocketHandler() {
        super(NAME, URL, (String content) -> {});
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

    public void sendPagesRemove(Stream<TocItem> removedTocItems) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "pagesRemove");
        payload.put("removedTocItems", removedTocItems.map(TocItem::toMap).collect(toList()));

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

    public void sendDocReferences(DocReferences docReferences) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "docReferencesUpdate");
        payload.put("docReferences", docReferences.toMap());

        send(payload);
    }

    public void sendGlobalAnchors(DocReferences docReferences) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "docReferencesUpdate");
        payload.put("docReferences", docReferences.toMap());

        send(payload);
    }

    public void sendFooter(Footer footer) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "footerUpdate");
        payload.put("footer", footer.toMap());

        send(payload);
    }

    public void sendError(String errorMessage, String stackTrace) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("message", errorMessage);
        error.put("stackTrace", stackTrace);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("type", "error");
        payload.put("error", error);

        send(payload);
    }

    private void send(Map<String, ?> payload) {
        if (sockets.isEmpty()) {
            ConsoleOutputs.out(BLUE, "preview send changes connection ", FontStyle.NORMAL, "with", BLUE, " web page ", FontStyle.NORMAL, "is not established. ",
                    BLUE, "reload or open", FontStyle.NORMAL, " the page");
            return;
        }

        Object typeVal = payload.get("type");
        String type = typeVal != null ? typeVal.toString() : "";
        ConsoleOutputs.out("preview sending: ", BLUE, type);

        send(URL, payload);
    }

    @Override
    public void onConnect(String uri) {
    }
}
