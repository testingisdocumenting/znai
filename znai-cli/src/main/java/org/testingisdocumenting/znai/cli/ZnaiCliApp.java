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

package org.testingisdocumenting.znai.cli;

import org.testingisdocumenting.znai.cli.extension.CliCommandConfig;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.AnsiConsoleOutput;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.console.ansi.FontStyle;
import org.testingisdocumenting.znai.html.HtmlPage;
import org.testingisdocumenting.znai.server.AuthorizationHeaderBasedAuthenticationHandler;
import org.testingisdocumenting.znai.server.HttpServerUtils;
import org.testingisdocumenting.znai.server.SslConfig;
import org.testingisdocumenting.znai.server.ZnaiServer;
import org.testingisdocumenting.znai.server.preview.DocumentationPreview;
import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.website.ProgressReporter;
import org.testingisdocumenting.znai.website.WebResource;
import org.testingisdocumenting.znai.website.WebSite;
import org.testingisdocumenting.znai.website.modifiedtime.ConstantPageModifiedTime;
import org.testingisdocumenting.znai.website.modifiedtime.FileBasedPageModifiedTime;
import org.testingisdocumenting.znai.website.modifiedtime.PageModifiedTimeStrategy;
import io.vertx.core.http.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.stream.Stream;

public class ZnaiCliApp {
    protected static final String PLUGIN_PARAMS_FILE_NAME = "plugin-params.json";

    private final ZnaiCliConfig config;
    private final Path deployPath;
    private final SslConfig sslConfig;
    private WebSite webSite;

    public ZnaiCliApp(ZnaiCliConfig cliConfig) {
        System.setProperty("java.awt.headless", "true");
        this.config = cliConfig;
        this.sslConfig = cliConfig.createSslConfig();
        this.deployPath = config.getDeployRoot().resolve(getDocId());
    }

    public static void start(ZnaiCliConfig cliConfig) {
        ZnaiCliApp cliApp = new ZnaiCliApp(cliConfig);
        cliApp.start();
    }

    public static void main(String[] args) {
        ZnaiCliConfig cliConfig = new ZnaiCliConfig(System::exit, args);
        if (!cliConfig.isPrintOutsideDepsMode()) {
            ConsoleOutputs.add(new AnsiConsoleOutput());
        }
        start(cliConfig);
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

        if (needsDocGeneration()) {
            webSite = generateDocs(config.getSourceRoot());
            exportLlmTxtIfNeeded();
        }

        if (config.isPreviewMode()) {
            preview();
        } else if (config.isServeMode()) {
            serve();
        } else if (config.isExportMode()) {
            export();
        } else if (config.isPrintOutsideDepsMode()) {
            printOutsideDeps();
        } else if (config.isCustomCommand()) {
            config.getSpecifiedCustomCommand().handle(
                    new CliCommandConfig(config.getDocId(), config.getSourceRoot(), config.getDeployRoot(), config.getActor()));
        }
    }

    private boolean needsDocGeneration() {
        if (config.isServeMode() || config.isPreviewMode()) {
            return false;
        }

        if (config.isCustomCommand()) {
            return config.getSpecifiedCustomCommand().needsDocGeneration();
        }

        return true;
    }

    private void preview() {
        DocumentationPreview preview = new DocumentationPreview(config.getSourceRoot(), config.getDeployRoot());
        preview.start(this::generateDocs, config.createSslConfig(), config.getPort(), () -> reportHostPort(config.getPort(), "/preview"));
    }

    private void serve() {
        HttpServer server = new ZnaiServer(config.getDeployRoot(),
                new AuthorizationHeaderBasedAuthenticationHandler(),
                config.createSslConfig()).create();
        HttpServerUtils.listen(server, config.getPort());

        reportHostPort(config.getPort(), "");
    }

    public void export() {
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
        FileUtils.writeTextContent(lookupPath, artifactsDirName);
    }

    public void printOutsideDeps() {
        Path sourceRoot = config.getSourceRoot();

        webSite.getOutsideDocsRequestedResources().values().stream()
                .map(fullPath -> sourceRoot.relativize(fullPath).toString())
                .sorted()
                .forEach(System.out::println);
    }

    private void exportLlmTxtIfNeeded() {
        Path llmTxtOutputPath = config.getLlmTxtOutputPath();
        if (llmTxtOutputPath == null) {
            return;
        }

        Path fullOutputPath = llmTxtOutputPath.isAbsolute() ?
                llmTxtOutputPath :
                config.getSourceRoot().resolve(llmTxtOutputPath);

        ProgressReporter.reportPhase("exporting llm.txt");
        Path llmTxtSource = deployPath.resolve("llm.txt");
        copyFile(llmTxtSource, fullOutputPath);
    }

    private static void copyFile(Path source, Path target) {
        ConsoleOutputs.out("copy ", Color.PURPLE, source, Color.YELLOW, " to ", Color.PURPLE, target);
        FileUtils.copyFile(source, target);
    }

    public static void copyDir(Path source, Path target) {
        try (Stream<Path> discoveredFiles = Files.walk(source)) {
            discoveredFiles
                    .filter(Files::isRegularFile)
                    .forEach(f -> copyFile(f, target.resolve(source.relativize(f))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WebSite generateDocs(Path sourceRoot) {
        Path userDefinedFavicon = sourceRoot.resolve("favicon.png");
        WebResource favIconResource = Files.exists(userDefinedFavicon) ?
                WebResource.withPath(userDefinedFavicon, HtmlPage.FAVICON_PATH):
                WebResource.fromResource(HtmlPage.FAVICON_PATH);

        String llmUrlPrefix = System.getenv("ZNAI_LLM_URL_PREFIX");
        WebSite.Configuration webSiteCfg = WebSite.withRoot(sourceRoot).
                withId(getDocId()).
                withDocumentationType(config.getMarkupType()).
                withMetaFromJsonFile(sourceRoot.resolve(DocMeta.META_FILE_NAME)).
                withFileWithLookupPaths("lookup-paths").
                withAdditionalLookupPaths(config.getLookupPaths()).
                withFooterPath(sourceRoot.resolve("footer.md")).
                withExtensionsDefPath(sourceRoot.resolve("extensions.json")).
                withRedirectsPath(sourceRoot.resolve("page-redirects.csv")).
                withGlobalReferencesPathNoExt(sourceRoot.resolve("references")).
                withGlobalPluginParamsPath(sourceRoot.resolve(PLUGIN_PARAMS_FILE_NAME)).
                withWebResources(favIconResource).
                withPageModifiedTimeStrategy(pageModifiedTimeStrategy()).
                withEnabledPreview(config.isPreviewMode()).
                withValidateExternalLinks(config.isValidateExternalLinks()).
                withLlmUrlPrefix(llmUrlPrefix == null ? "" : llmUrlPrefix);

        return config.isExportMode() || config.isPrintOutsideDepsMode() ?
                webSiteCfg.parseOnly():
                webSiteCfg.deployTo(deployPath);
    }

    private PageModifiedTimeStrategy pageModifiedTimeStrategy() {
        return switch (config.getModifiedTimeStrategy()) {
            case FILE -> new FileBasedPageModifiedTime();
            case CONSTANT -> new ConstantPageModifiedTime(Instant.now());
            default -> null;
        };
    }

    private boolean maybeCreateNewFromTemplate(DocScaffolding scaffolding) {
        String templatePathEnv = System.getenv("ZNAI_NEW_TEMPLATE_PATH");
        if (templatePathEnv == null) {
            return false;
        }

        if (templatePathEnv.trim().isEmpty()) {
            ConsoleOutputs.out(Color.RED, "template path value is empty");
            return false;
        }

        Path templatePath = Paths.get(templatePathEnv);
        if (Files.exists(templatePath) && Files.isDirectory(templatePath)) {
            ConsoleOutputs.out(Color.BLUE, "scaffolding from template: ", Color.PURPLE, templatePath);
            scaffolding.createFromFileSystem(templatePath);
            return true;
        } else {
            ConsoleOutputs.err(Color.RED, "template path does not exist or is not a directory: ",
                    Color.PURPLE, templatePath);
            ConsoleOutputs.out(Color.YELLOW, "falling back to default scaffold");
            return false;
        }
    }

    private void createNew() {
        Path pathToScaffold = (config.isSourceRootSet() ?
                config.getSourceRoot() :
                Paths.get("guide")).toAbsolutePath();

        ConsoleOutputs.out(Color.BLUE, "scaffolding new documentation: ", Color.PURPLE, pathToScaffold);
        DocScaffolding scaffolding = new DocScaffolding(pathToScaffold);

        if (!maybeCreateNewFromTemplate(scaffolding)) {
            scaffolding.create();
        }
    }

    private void announceMode(String name) {
        ConsoleOutputs.out(Color.BLUE, ZnaiTitleWithMaybeOverride.title, " ", Color.YELLOW, name + " mode");
    }

    private void reportHostPort(int port, String relativeUrl) {
        try {
            ConsoleOutputs.out(Color.BLUE, "server started ", FontStyle.NORMAL, sslConfig.isSpecified() ? "https://" : "http://", InetAddress.getLocalHost().getHostName(), ":",
                    port, relativeUrl.isEmpty() ? "" : relativeUrl);
        } catch (UnknownHostException e) {
            ConsoleOutputs.err("Cannot extract host name");
        }
    }
}
