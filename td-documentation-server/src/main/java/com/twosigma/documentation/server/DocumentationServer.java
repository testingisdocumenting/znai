package com.twosigma.documentation.server;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * @author mykola
 */
public class DocumentationServer {
    public static HttpServer create(ServerConfig serverConfig) {
        System.out.println("server configuration:\n" + serverConfig);

        System.setProperty("vertx.cwd", serverConfig.getRootOfDocs().toString());

        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        StaticHandler pagesStaticHandler = StaticHandler.create(".").
                setFilesReadOnly(false).setMaxAgeSeconds(0);

        StaticHandler staticCommonResources =
                StaticHandler.create("static").setMaxAgeSeconds(600);

        router.get("/static/*").handler(staticCommonResources);

        router.get("/:product/:section/:page").handler(rc -> {
            MultiMap params = rc.request().params();
            System.out.println(params);

            rc.next();
        });

        router.get("/*").handler(pagesStaticHandler);

        server.requestHandler(router::accept);

        return server;
    }
}
