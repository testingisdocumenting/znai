package com.twosigma.documentation.server;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.server.docpreparation.DocumentationPreparationHandlers;
import com.twosigma.documentation.server.docpreparation.DocumentationPreparationTestHandler;
import com.twosigma.documentation.server.docpreparation.DocumentationPreparationWebSocketHandler;
import com.twosigma.documentation.server.sockets.JsonWebSocketHandler;
import com.twosigma.documentation.server.sockets.JsonWebSocketHandlerComposition;
import com.twosigma.documentation.server.upload.FileUploadHandler;
import com.twosigma.documentation.server.upload.UnzipTask;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.impl.RoutingContextDecorator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class DocumentationServer {
    private final JsonWebSocketHandlerComposition socketHandlers;
    private final DocumentationPreparationWebSocketHandler documentationPreparationWebSocketHandler;
    private final Vertx vertx;
    private Path deployRoot;

    public DocumentationServer(Path deployRoot) {
        this.deployRoot = deployRoot;

        System.setProperty("vertx.cwd", deployRoot.toString());
        System.setProperty("file.encoding","UTF-8");

        createDirs(deployRoot);
        vertx = Vertx.vertx();

        socketHandlers = new JsonWebSocketHandlerComposition();
        documentationPreparationWebSocketHandler = new DocumentationPreparationWebSocketHandler(vertx);
        
        socketHandlers.add(documentationPreparationWebSocketHandler);

        DocumentationPreparationHandlers.add(new DocumentationPreparationTestHandler());
    }

    public void addSocketHandler(JsonWebSocketHandler socketHandler) {
        socketHandlers.add(socketHandler);
    }

    public HttpServer create() {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        StaticHandler pagesStaticHandler = StaticHandler.create(".").
                setFilesReadOnly(false).setMaxAgeSeconds(0);

        StaticHandler staticCommonResources =
                StaticHandler.create("static").setMaxAgeSeconds(600);

        // delegate each documentation static resources to a central copy
        // to make existing docs served centrally upgrade all at once
        router.get("/:docId/static/*").handler(ctx -> {
            MultiMap params = ctx.request().params();

            staticCommonResources.handle(new RoutingContextDecorator(
                    router.route("/" + params.get("docId") + "/static"), ctx));
        });

        router.route("/upload/:docId").handler(ctx -> {
            MultiMap params = ctx.request().params();
            String docId = params.get("docId");
            new FileUploadHandler(vertx, deployRoot, docId, (p) -> unzip(deployRoot, docId, p)).handle(ctx.request());
        });

        router.get("/*").handler(pagesStaticHandler);

        server.websocketHandler(socketHandlers);
        server.requestHandler(router::accept);

        return server;
    }

    private static void createDirs(Path deployRoot) {
        try {
            Files.createDirectories(deployRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void unzip(Path root, String docId, Path path) {
        ConsoleOutputs.out(Color.BLUE, "unzipping docs: ", Color.PURPLE, path, Color.BLACK, " to ",
                Color.PURPLE, root);

        UnzipTask unzipTask = new UnzipTask(root.resolve(path), root);
        unzipTask.execute();
        ConsoleOutputs.out(Color.BLUE, "unzipped docs: ", Color.PURPLE, root);
    }

    // this is entry point for local development and testing
    // official mdoc start is through com.twosigma.documentation.cli.DocumentationCliApp
    public static void main(String[] args) {
        ConsoleOutputs.add(new AnsiConsoleOutput());

        HttpServer server = new DocumentationServer(Paths.get("")).create();
        server.listen(3333);
        System.out.println("test server started");
    }
}
