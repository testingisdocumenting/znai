package com.twosigma.documentation.client;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.WebSite;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class DocumentationClientApp {
    private final ClientConfig cfg;

    public DocumentationClientApp(ClientConfig cfg) {
        this.cfg = cfg;
    }

    public static void main(String[] args) {
        ConsoleOutputs.add(new AnsiConsoleOutput());
        ClientConfig clientConfig = new ClientConfig(args);

        new DocumentationClientApp(clientConfig).start();
    }

    private void start() {
        Path path = generateDocs();
        Path zipPath = zipDocs(path);
        upload(zipPath);
    }

    private Path generateDocs() {
        ConsoleOutputs.out(Color.BLUE, "generating documentation");

        final Path deployPath = Paths.get("for-deploy/documentation");
        final Path mdRoot = Paths.get("test-documentation");

        WebSite.withToc(mdRoot.resolve("toc")).
                withMetaFromJsonFile(mdRoot.resolve("meta.json")).
                deployTo(deployPath);

        return deployPath;
    }

    private Path zipDocs(Path dirToZip) {
        ConsoleOutputs.out(Color.BLUE, "zipping: ", Color.PURPLE, dirToZip);

        Path zipDestination = Paths.get("for-deploy.zip");
        ZipTask zipTask = new ZipTask(dirToZip, zipDestination);
        zipTask.execute();

        return zipDestination;
    }

    private void upload(Path path) {
        ConsoleOutputs.out(Color.BLUE, "uploading documentation: ", Color.GREEN,
                path, Color.BLACK, " to ", Color.PURPLE,  cfg.getDocId());

        DocumentationUploader uploader = new DocumentationUploader(this::onUploadFinish);
        uploader.upload(cfg.getDocId(), path);
    }

    private void onUploadFinish(int statusCode) {
        if (statusCode == 200) {
            ConsoleOutputs.out(Color.BLUE, "upload finished");
        } else {
            ConsoleOutputs.err("upload failed ", statusCode);
        }

        System.exit(0);
    }
}
