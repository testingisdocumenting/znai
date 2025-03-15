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

import io.vertx.core.http.HttpServer;
import org.apache.commons.io.FileUtils;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.server.*;
import org.testingisdocumenting.znai.server.sockets.WebSocketHandlers;
import org.testingisdocumenting.znai.website.WebSite;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Stream;

public class DocumentationPreview {
    private static final ExecutorService SINGLE_THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();

    private final Path sourceRoot;
    private final Path deployRoot;

    private WebSite webSite;
    private FileWatcher fileWatcher;
    private Function<Path, WebSite> createWebSite;
    private PreviewSendChangesWebSocketHandler previewSendChangesWebSocketHandler;
    private PreviewUpdatePathWebSocketHandler previewUpdatePathWebSocketHandler;

    public DocumentationPreview(Path sourceRoot, Path deployRoot) {
        this.sourceRoot = sourceRoot;
        this.deployRoot = deployRoot;
    }

    public void start(Function<Path, WebSite> createWebSite, SslConfig sslConfig, int port, Runnable onStart) {
        this.createWebSite = createWebSite;

        ZnaiServer znaiServer = new ZnaiServer(deployRoot, new NoAuthenticationHandler(), sslConfig);
        previewSendChangesWebSocketHandler = new PreviewSendChangesWebSocketHandler();
        previewUpdatePathWebSocketHandler = new PreviewUpdatePathWebSocketHandler(this::changePreviewSourceRoot);

        WebSocketHandlers.add(previewSendChangesWebSocketHandler);
        WebSocketHandlers.add(previewUpdatePathWebSocketHandler);
        ConsoleOutputs.add(previewUpdatePathWebSocketHandler);
        HttpServer server = znaiServer.create();

        reportPhase("starting server");
        HttpServerUtils.listen(server, port);

        buildWebSiteAndFileWatcher(sourceRoot);
        onStart.run();
        fileWatcher.start();
    }

    public void changePreviewSourceRoot(Path sourceRoot) {
        SINGLE_THREAD_POOL_EXECUTOR.submit(() -> {
            try {
                clearFileWatcher();
                cleanDeployRoot();
                buildWebSiteAndFileWatcher(sourceRoot);
                sendCompletionSignal();
                new Thread(() -> fileWatcher.start()).start();
            } catch (Throwable e) {
                ConsoleOutputs.err(e.getMessage());
            }
        });
    }

    private void sendCompletionSignal() {
        previewUpdatePathWebSocketHandler.sendCompletion();
    }

    private void buildWebSiteAndFileWatcher(Path sourceRoot) {
        reportPhase("building webs site", sourceRoot);
        webSite = createWebSite.apply(sourceRoot);

        reportPhase("initializing file watcher");
        final PreviewPushFileChangeHandler fileChangeHandler = new PreviewPushFileChangeHandler(
                previewSendChangesWebSocketHandler,
                webSite);
        fileWatcher = new FileWatcher(
                webSite.getCfg(),
                Stream.concat(
                        webSite.getAuxiliaryFilesRegistry().getAllPaths(),
                        webSite.getToc().getResolvedPaths().stream()),
                fileChangeHandler);

        webSite.getAuxiliaryFilesRegistry().registerListener(fileWatcher);
        webSite.registerTocChangeListener(fileWatcher);
    }

    private void cleanDeployRoot() {
        var pathToClean = deployRoot.resolve("preview");
        reportPhase("cleaning deploy root", pathToClean);
        FileUtils.deleteQuietly(pathToClean.toFile());
    }

    private void clearFileWatcher() {
        reportPhase("unsubscribing from file changes");
        webSite.unregisterTocChangeListener(fileWatcher);
        webSite.getAuxiliaryFilesRegistry().unregisterListener(fileWatcher);

        reportPhase("stopping file watcher");
        fileWatcher.stop();
    }

    private void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
    }

    private void reportPhase(String phase, Object param) {
        ConsoleOutputs.out(Color.BLUE, phase, " ", Color.PURPLE, param);
    }
}
