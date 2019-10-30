/*
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

package com.twosigma.znai.cli;

import com.twosigma.znai.cli.extension.CliCommandConfig;
import com.twosigma.znai.console.ConsoleOutputs;
import com.twosigma.znai.console.ansi.AnsiConsoleOutput;
import com.twosigma.znai.console.ansi.Color;
import com.twosigma.znai.html.HtmlPage;
import com.twosigma.znai.html.reactjs.ReactJsBundle;
import com.twosigma.znai.server.DocumentationServer;
import com.twosigma.znai.server.preview.DocumentationPreview;
import com.twosigma.znai.utils.FileUtils;
import com.twosigma.znai.web.WebResource;
import com.twosigma.znai.website.ProgressReporter;
import com.twosigma.znai.website.WebSite;
import io.vertx.core.http.HttpServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ZnaiCliApp {
    private ZnaiCliConfig config;
    private Path deployPath;
    private WebSite webSite;
    private ReactJsBundle reactJsBundle;

    public ZnaiCliApp(ZnaiCliConfig cliConfig) {
        System.setProperty("java.awt.headless", "true");
        this.config = cliConfig;
        this.deployPath = config.getDeployRoot().resolve(getDocId());
    }

    public static void start(ZnaiCliConfig cliConfig) {
        ZnaiCliApp cliApp = new ZnaiCliApp(cliConfig);
        cliApp.start();
    }

    public static void main(String[] args) {
        ConsoleOutputs.add(new AnsiConsoleOutput());
        start(new ZnaiCliConfig(args));
    }

    private String getDocId() {
        if (config.isPreviewMode()) {
            return "preview";
        }

        return config.getDocId();
    }

    private void start() {
        config.print();

        if (config.isScaffoldMode()) {
            createNew();
            return;
        }

        announceMode(config.getModeAsString());

        reactJsBundle = new ReactJsBundle();

        if (! config.isServeMode()) {
            generateDocs();
        }

        if (config.isPreviewMode()) {
            preview();
        } else if (config.isServeMode()) {
            serve();
        } else if (config.isExportMode()) {
            export();
        } else if (config.isCustomCommand()) {
            config.getSpecifiedCustomCommand().handle(
                    new CliCommandConfig(config.getDocId(), config.getSourceRoot(), config.getDeployRoot(), config.getActor()));
        }
    }

    private void preview() {
        DocumentationPreview preview = new DocumentationPreview(config.getSourceRoot(), config.getDeployRoot());
        preview.start(webSite, config.getPort());
    }

    private void serve() {
        HttpServer server = new DocumentationServer(reactJsBundle, config.getDeployRoot()).create();
        server.listen(config.getPort());
    }

    private void export() {
        ProgressReporter.reportPhase("export documentation files");
        copyDir(config.getSourceRoot(), config.getExportRoot());

        String artifactsDirName = "_artifacts";
        Path artifactsRoot = config.getExportRoot().resolve(artifactsDirName);

        ProgressReporter.reportPhase("export files that are required but outside of documentation");
        webSite.getOutsideDocsRequestedResources().forEach((k, v) -> {
            Path dest = artifactsRoot.resolve(k);
            copyFile(v, dest);
        });

        ProgressReporter.reportPhase("patching lookup-paths");
        Path lookupPath = config.getExportRoot().resolve("lookup-paths");
        String lookupContent = FileUtils.fileTextContent(lookupPath);
        FileUtils.writeTextContent(lookupPath, artifactsDirName + "\n" + lookupContent);
    }

    private static void copyFile(Path source, Path target) {
        ConsoleOutputs.out("copy ", Color.PURPLE, source, Color.YELLOW, " to ", Color.PURPLE, target);
        FileUtils.copyFile(source, target);
    }

    public static void copyDir(Path source, Path target) {
        try {
            Files
                .walk(source)
                .filter(Files::isRegularFile)
                .forEach(f -> copyFile(f, target.resolve(source.relativize(f))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateDocs() {
        Path userDefinedFavicon = config.getSourceRoot().resolve("favicon.png");
        WebResource favIconResource = Files.exists(userDefinedFavicon) ?
                WebResource.withPath(userDefinedFavicon, HtmlPage.FAVICON_PATH):
                WebResource.fromResource(HtmlPage.FAVICON_PATH);

        WebSite.Configuration webSiteCfg = WebSite.withRoot(config.getSourceRoot()).
                withReactJsBundle(reactJsBundle).
                withId(getDocId()).
                withDocumentationType(config.getMarkupType()).
                withMetaFromJsonFile(config.getSourceRoot().resolve("meta.json")).
                withFileWithLookupPaths("lookup-paths").
                withFooterPath(config.getSourceRoot().resolve("footer.md")).
                withExtensionsDefPath(config.getSourceRoot().resolve("extensions.json")).
                withWebResources(favIconResource).
                withEnabledPreview(config.isPreviewMode());

        this.webSite = config.isExportMode() ?
                webSiteCfg.parseOnly():
                webSiteCfg.deployTo(deployPath);
    }

    private void createNew() {
        ConsoleOutputs.out(Color.BLUE, "scaffolding new documentation");
        DocScaffolding scaffolding = new DocScaffolding(Paths.get("znai"));
        scaffolding.create();
    }

    private void announceMode(String name) {
        ConsoleOutputs.out(Color.BLUE, "znai ", Color.YELLOW, name + " mode");
    }
}
