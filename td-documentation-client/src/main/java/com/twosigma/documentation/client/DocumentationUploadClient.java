package com.twosigma.documentation.client;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.streams.Pump;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class DocumentationUploadClient {
    private final Vertx vertx;
    private final String docId;
    private final Path deployRoot;
    private final String host;
    private final int port;

    public DocumentationUploadClient(String docId, Path deployRoot, String host, int port) {
        this.vertx = Vertx.vertx();
        this.docId = docId;
        this.deployRoot = deployRoot;
        this.host = host;
        this.port = port;
    }

    public void upload(OnUploadFinishedClientHandler onUploadFinish) {
        Path zipPath = zipDocs(deployRoot);
        upload(zipPath, onUploadFinish);
    }

    private Path zipDocs(Path dirToZip) {
        ConsoleOutputs.out(Color.BLUE, "zipping: ", Color.PURPLE, dirToZip);

        Path zipDestination = Paths.get(docId + ".zip");
        zipDestination.toFile().deleteOnExit();
        ZipTask zipTask = new ZipTask(dirToZip, zipDestination);
        zipTask.execute();

        return zipDestination;
    }

    private void upload(Path path, OnUploadFinishedClientHandler onUploadFinish) {
        ConsoleOutputs.out(Color.BLUE, "uploading documentation: ", Color.GREEN,
                path, Color.BLACK, " to ", Color.PURPLE, fullUrl(docId));

        HttpClientRequest req = vertx.createHttpClient(new HttpClientOptions()).put(port, host,
                "/upload/" + docId, resp -> handleUploadFinish(resp.statusCode(), onUploadFinish));

        FileSystem fs = vertx.fileSystem();
        fs.props(path.toString(), ah -> {
            FileProps props = ah.result();

            long size = props.size();

            req.headers().set("content-length", String.valueOf(size));
            fs.open(path.toString(), new OpenOptions(), oh -> {
                AsyncFile file = oh.result();
                Pump pump = Pump.pump(file, req);
                file.endHandler(v -> req.end());
                pump.start();
            });
        });
    }

    private String fullUrl(String docId) {
        return host +
                (port != 80 && port != 443 ? ":" + port : "") +
                "/" + docId;
    }

    private void handleUploadFinish(int statusCode, OnUploadFinishedClientHandler onUploadFinish) {
        if (statusCode == 200) {
            ConsoleOutputs.out(Color.BLUE, "upload finished");
        } else {
            ConsoleOutputs.err("upload failed ", statusCode);
        }

        onUploadFinish.onUpload(statusCode);
        vertx.close();
    }
}
