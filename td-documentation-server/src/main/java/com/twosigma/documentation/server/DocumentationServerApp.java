package com.twosigma.documentation.server;

import com.twosigma.documentation.server.preview.PreviewWebSocketHandler;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * @author mykola
 */
public class DocumentationServerApp {
    public static void main(String[] args) {
        ServerConfig serverConfig = new ServerConfig(args);
        HttpServer server = DocumentationServer.create(serverConfig);
        server.listen(8080);
    }
}
