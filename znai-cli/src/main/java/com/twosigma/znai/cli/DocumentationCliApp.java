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

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.AnsiConsoleOutput;
import com.twosigma.console.ansi.Color;
import com.twosigma.znai.cli.extension.CliCommandConfig;
import com.twosigma.znai.html.HtmlPage;
import com.twosigma.znai.html.reactjs.ReactJsBundle;
import com.twosigma.znai.parser.MarkupTypes;
import com.twosigma.znai.server.DocumentationServer;
import com.twosigma.znai.server.preview.DocumentationPreview;
import com.twosigma.znai.web.WebResource;
import com.twosigma.znai.website.WebSite;
import io.vertx.core.http.HttpServer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentationCliApp {
    private DocumentationCliConfig config;
    private Path deployPath;
    private WebSite webSite;
    private ReactJsBundle reactJsBundle;

    public DocumentationCliApp(DocumentationCliConfig cliConfig) {
        System.setProperty("java.awt.headless", "true");
        this.config = cliConfig;
        this.deployPath = config.getDeployRoot().resolve(getDocId());
    }

    public static void start(DocumentationCliConfig cliConfig) {
        DocumentationCliApp cliApp = new DocumentationCliApp(cliConfig);
        cliApp.start();
    }

    public static void main(String[] args) {
        ConsoleOutputs.add(new AnsiConsoleOutput());
        start(new DocumentationCliConfig(args));
    }

    private String getDocId() {
        if (config.isPreview()) {
            return "preview";
        }

        return config.getDocId();
    }

    private void start() {
        config.print();

        if (config.isNew()) {
            createNew();
            return;
        }

        announceMode(config.getModeAsString());

        reactJsBundle = new ReactJsBundle();

        if (! config.isServe()) {
            generateDocs();
        }

        if (config.isPreview()) {
            preview();
        } else if (config.isServe()) {
            serve();
        } else if (config.isCustom()) {
            config.getSpecifiedCustomCommand().handle(
                    new CliCommandConfig(config.getDocId(), config.getSourceRoot(), config.getDeployRoot()));
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

    private void generateDocs() {
        Path userDefinedFavicon = config.getSourceRoot().resolve("favicon.png");
        WebResource favIconResource = Files.exists(userDefinedFavicon) ?
                WebResource.withPath(userDefinedFavicon, HtmlPage.FAVICON_PATH):
                WebResource.fromResource(HtmlPage.FAVICON_PATH);

        webSite = WebSite.withToc(resolveTocPath()).
                withReactJsBundle(reactJsBundle).
                withId(getDocId()).
                withMarkupType(config.getMarkupType()).
                withMetaFromJsonFile(config.getSourceRoot().resolve("meta.json")).
                withFileWithLookupPaths("lookup-paths").
                withFooterPath(config.getSourceRoot().resolve("footer.md")).
                withExtensionsDefPath(config.getSourceRoot().resolve("extensions.json")).
                withWebResources(favIconResource).
                withEnabledPreview(config.isPreview()).deployTo(deployPath);
    }

    private Path resolveTocPath() {
        switch (config.getMarkupType()) {
            case MarkupTypes.SPHINX:
                return config.getSourceRoot().resolve("index.xml");
            default:
                return config.getSourceRoot().resolve("toc");
        }
    }

    private void createNew() {
        ConsoleOutputs.out(Color.BLUE, "scaffolding new documentation");
        DocScaffolding scaffolding = new DocScaffolding(Paths.get("mdoc"));
        scaffolding.create();
    }

    private void announceMode(String name) {
        ConsoleOutputs.out(Color.BLUE, "mdoc ", Color.YELLOW, name + " mode");
    }
}
