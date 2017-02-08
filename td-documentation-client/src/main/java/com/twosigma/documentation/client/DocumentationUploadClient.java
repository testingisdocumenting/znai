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
    private String docId;
    private Path deployRoot;
    private String host;
    private int port;

    public DocumentationUploadClient(String docId, Path deployRoot, String host, int port) {
        this.docId = docId;
        this.deployRoot = deployRoot;
        this.host = host;
        this.port = port;
    }

    public void upload(UploadFinishedHandler onUploadFinish) {
        Path zipPath = zipDocs(deployRoot);
        upload(zipPath, onUploadFinish);
    }

    private Path zipDocs(Path dirToZip) {
        ConsoleOutputs.out(Color.BLUE, "zipping: ", Color.PURPLE, dirToZip);

        Path zipDestination = Paths.get("for-upload.zip");
        zipDestination.toFile().deleteOnExit();
        ZipTask zipTask = new ZipTask(dirToZip, zipDestination);
        zipTask.execute();

        return zipDestination;
    }

    private void upload(Path path, UploadFinishedHandler onUploadFinish) {
        ConsoleOutputs.out(Color.BLUE, "uploading documentation: ", Color.GREEN,
                path, Color.BLACK, " to ", Color.PURPLE, docId);

        Vertx vertx = Vertx.vertx();
        HttpClientRequest req = vertx.createHttpClient(new HttpClientOptions()).put(port, host,
                "/upload/" + docId, resp -> {
                    handleUploadFinish(resp.statusCode(), onUploadFinish);
                });

        FileSystem fs = vertx.fileSystem();
        fs.props(path.toString(), ah -> {
            FileProps props = ah.result();
            System.out.println(props);

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

    private void handleUploadFinish(int statusCode, UploadFinishedHandler onUploadFinish) {
        if (statusCode == 200) {
            ConsoleOutputs.out(Color.BLUE, "upload finished");
        } else {
            ConsoleOutputs.err("upload failed ", statusCode);
        }

        onUploadFinish.onUpload(statusCode);
    }

}
