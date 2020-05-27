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

package org.testingisdocumenting.znai.server.preview;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.server.ZnaiServer;
import org.testingisdocumenting.znai.server.sockets.WebSocketHandlers;
import org.testingisdocumenting.znai.website.WebSite;
import io.vertx.core.http.HttpServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class DocumentationPreview {
    private final Path deployRoot;

    public DocumentationPreview(Path deployRoot) {
        this.deployRoot = deployRoot;
    }

    public void start(WebSite webSite, int port, Runnable onStart) {
        ZnaiServer znaiServer = new ZnaiServer(webSite.getReactJsBundle(), deployRoot);
        PreviewWebSocketHandler previewWebSocketHandler = new PreviewWebSocketHandler();

        WebSocketHandlers.add(previewWebSocketHandler);

        HttpServer server = znaiServer.create();

        reportPhase("starting server");
        final PreviewPushFileChangeHandler fileChangeHandler = new PreviewPushFileChangeHandler(
                previewWebSocketHandler,
                webSite);
        server.listen(port);

        reportPhase("initializing file watcher");
        final FileWatcher fileWatcher = new FileWatcher(
                webSite.getCfg(),
                webSite.getAuxiliaryFilesRegistry().getAllPaths(),
                fileChangeHandler);

        webSite.getAuxiliaryFilesRegistry().registerListener(fileWatcher);

        onStart.run();

        fileWatcher.start();
    }

    private void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
    }
}
