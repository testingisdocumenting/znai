package com.twosigma.documentation.server.upload;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.streams.Pump;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author mykola
 */
public class FileUploadVertxHandler implements Handler<HttpServerRequest> {
    private Vertx vertx;
    private final String docId;
    private final Path destination;

    public FileUploadVertxHandler(Vertx vertx, String docId, Path deployRoot) {
        this.vertx = vertx;
        this.docId = docId;
        this.destination = deployRoot.resolve(docId + ".zip");
    }

    @Override
    public void handle(HttpServerRequest req) {
        req.pause();

        vertx.fileSystem().open(destination.toString(), new OpenOptions(), fh -> {
            AsyncFile file = fh.result();
            Pump pump = Pump.pump(req, file);
            req.endHandler(eh -> file.close(fch -> {
                req.response().end();

                OnUploadFinishedServerHandlers.onUploadFinished(docId, destination);
                deleteUploadedFile();
            }));

            pump.start();
            req.resume();
        });
    }

    private void deleteUploadedFile()  {
        try {
            Files.delete(destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
