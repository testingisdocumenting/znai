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

package org.testingisdocumenting.znai.server;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.impl.RoutingContextDecorator;
import org.testingisdocumenting.znai.html.HtmlPage;
import org.testingisdocumenting.znai.html.reactjs.HtmlReactJsPage;
import org.testingisdocumenting.znai.html.reactjs.ReactJsBundle;
import org.testingisdocumenting.znai.server.auth.AuthorizationHandlers;
import org.testingisdocumenting.znai.server.auth.AuthorizationRequestLink;
import org.testingisdocumenting.znai.server.docpreparation.DocumentationPreparationHandlers;
import org.testingisdocumenting.znai.server.docpreparation.NoOpDocumentationPreparationProgress;
import org.testingisdocumenting.znai.server.sockets.WebSocketHandlers;
import org.testingisdocumenting.znai.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ZnaiServer {
    private final Vertx vertx;
    private final ZnaiServerConfig serverConfig;
    private final AuthenticationHandler authenticationHandler;
    private final SslConfig sslConfig;
    private ZnaiCommands znaiCommands;

    public ZnaiServer(Path deployRoot, AuthenticationHandler authenticationHandler, SslConfig sslConfig) {
        this.serverConfig = new ZnaiServerConfig(deployRoot);
        this.authenticationHandler = authenticationHandler;
        this.sslConfig = sslConfig;

        System.setProperty("vertx.cwd", deployRoot.toString());
        System.setProperty("file.encoding","UTF-8");

        ServerLifecycleListeners.beforeStart(serverConfig);

        FileUtils.symlinkAwareCreateDirs(deployRoot);
        vertx = Vertx.vertx();
    }

    public void setZnaiCommands(ZnaiCommands znaiCommands) {
        this.znaiCommands = znaiCommands;
    }

    public HttpServer create() {
        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setCompressionSupported(true);
        sslConfig.updateServerOptions(httpServerOptions);
        HttpServer server = vertx.createHttpServer(httpServerOptions);

        Router router = Router.router(vertx);

        StaticHandler pagesStaticHandler = StaticHandler.create(".").
                setFilesReadOnly(false).setCachingEnabled(false);

        StaticHandler staticCommonResources =
                StaticHandler.create("static").setMaxAgeSeconds(600);

        // delegate each documentation static resources to a central copy
        // to make existing docs served centrally upgrade all at once
        router.get("/:docId/static/*").handler(ctx -> {
            MultiMap params = ctx.request().params();

            staticCommonResources.handle(new RoutingContextDecorator(
                    router.route("/" + params.get("docId") + "/static"), ctx));
        });

        router.get("/static/*").handler(staticCommonResources);
        router.get("/trigger/*").handler(this::triggerNewPreview);

        registerCustomHandlersAndRoutes(router);
        registerPagesHandler(router, pagesStaticHandler);

        WebSocketHandlers.init(vertx);
        server.websocketHandler(WebSocketHandlers.handler);
        server.requestHandler(router::accept);

        return server;
    }

    private void registerCustomHandlersAndRoutes(Router router) {
        UrlContentHandlers.urlContentHandlers()
                .forEach(urlContentHandler ->
                        router.get(urlContentHandler.url())
                                .handler(ctx -> ctx.response().end(
                                        urlContentHandler.buildContent(serverConfig, ctx, ReactJsBundle.INSTANCE))));

        RoutesProviders.register(vertx, serverConfig, router);
    }

    private void registerPagesHandler(Router router, StaticHandler pagesStaticHandler) {
        router.get("/*").handler(ctx -> {
            if (needToEndWithTrailingSlash(ctx)) {
                redirectToTrailingSlash(ctx);
                return;
            }

            String docId = extractDocId(ctx);

            if (!isAuthorized(ctx, docId)) {
                if (isNotDocPageRequest(ctx)) {
                    ctx.fail(403);
                }

                serverNotAuthorizedPage(ctx, docId);
                return;
            }

            if (DocumentationPreparationHandlers.isReady(docId)) {
                pagesStaticHandler.handle(ctx);
            } else if (isNotDocPageRequest(ctx)) {
                // znai documentations are single page apps
                // page refresh won't happen during navigation from one page to another
                // but images will still be requested if they are present on a page
                // in that case we will call documentation preparation before serving content
                vertx.executeBlocking(future -> {
                    DocumentationPreparationHandlers.prepare(docId, NoOpDocumentationPreparationProgress.INSTANCE);
                    future.complete();
                }, false, (res) -> pagesStaticHandler.handle(ctx));
            } else {
                serveDocumentationPreparationPage(ctx, docId);
            }
        });
    }

    private boolean isAuthorized(RoutingContext ctx, String docId) {
        String userId = authenticationHandler.authenticate(ctx, docId);
        if (userId.isEmpty()) {
            return true;
        }

        return AuthorizationHandlers.isAuthorized(userId, docId);
    }

    private void redirectToTrailingSlash(RoutingContext ctx) {
        ctx.response().putHeader("location", ctx.request().uri() + "/").setStatusCode(301).end();
    }

    private void serverNotAuthorizedPage(RoutingContext ctx, String docId) {
        HtmlReactJsPage htmlReactJsPage = new HtmlReactJsPage(ReactJsBundle.INSTANCE);
        Map<String, Object> props = new LinkedHashMap<>();

        props.put("docId", docId);
        props.put("allowedGroups", AuthorizationHandlers.allowedGroups(docId));

        AuthorizationRequestLink authorizationRequestLink = AuthorizationHandlers.authorizationRequestLink();
        props.put("authorizationRequestLink", authorizationRequestLink.getLink());
        props.put("authorizationRequestMessage", authorizationRequestLink.getMessage());

        HtmlPage htmlPage = htmlReactJsPage.create(docId + ": Not Authorized",
                "NotAuthorizedScreen", props, () -> "", FavIcons.DEFAULT_ICON_PATH);

        ctx.response().end(htmlPage.render(docId));
    }

    private void triggerNewPreview(RoutingContext ctx) {
        String uri = ctx.request().uri();
        String[] parts = uri.split("/");
//        ctx.response().end("test:" + List.of(parts));

        Path newPath = Paths.get("/Users/mykolagolubyev/work/testingisdocumenting/znai/znai-docs/target/znai");
        HtmlReactJsPage htmlReactJsPage = new HtmlReactJsPage(ReactJsBundle.INSTANCE);
        HtmlPage htmlPage = htmlReactJsPage.create("Changing source root",
                "PreviewChangeScreen", Collections.singletonMap("newPath", newPath.toString()), () -> "", FavIcons.DEFAULT_ICON_PATH);

        ctx.response().end(htmlPage.render("preview"));
//        znaiCommands.changePreviewSourceRoot(Paths.get("/Users/mykolagolubyev/work/testingisdocumenting/znai/znai-docs/znai"));
        znaiCommands.changePreviewSourceRoot(newPath);
    }

    private void serveDocumentationPreparationPage(RoutingContext ctx, String docId) {
        HtmlReactJsPage htmlReactJsPage = new HtmlReactJsPage(ReactJsBundle.INSTANCE);
        Map<String, Object> props = new LinkedHashMap<>();

        props.put("docId", docId);
        props.put("statusMessage", "checking documentation cache");
        props.put("progressPercent", 0);
        props.put("keyValues", Collections.emptyList());

        HtmlPage htmlPage = htmlReactJsPage.create("Preparing " + docId,
                "DocumentationPreparationScreen", props, () -> "", FavIcons.DEFAULT_ICON_PATH);

        ctx.response().end(htmlPage.render(docId));
    }

    private static boolean needToEndWithTrailingSlash(RoutingContext ctx) {
        if (isNotDocPageRequest(ctx)) {
            return false;
        }

        return !ctx.request().uri().endsWith("/");
    }

    private static String extractDocId(RoutingContext ctx) {
        String uri = ctx.request().uri();
        String[] parts = uri.split("/");

        return parts.length < 2 ? "" : parts[1];
    }

    private static boolean isNotDocPageRequest(RoutingContext ctx) {
        String uri = ctx.request().uri();
        int dotIdx = uri.lastIndexOf('.');
        if (dotIdx == -1) {
            return false;
        }

        int slashIdx = uri.lastIndexOf('/');
        if (slashIdx > dotIdx) {
            return false;
        }

        String extension = uri.substring(dotIdx);
        return !extension.equals("html");
    }
}
