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

package org.testingisdocumenting.znai.server.preview;

import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.html.DocPageReactProps;
import org.testingisdocumenting.znai.html.HtmlPageAndPageProps;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.core.DocMeta;
import org.testingisdocumenting.znai.structure.Footer;
import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.website.TocAddedUpdatedAndRemovedPages;
import org.testingisdocumenting.znai.website.WebSite;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.console.ansi.Color.BLUE;

public class PreviewPushFileChangeHandler implements FileChangeHandler {
    private final PreviewSendChangesWebSocketHandler previewSocket;
    private final WebSite previewWebSite;

    public PreviewPushFileChangeHandler(final PreviewSendChangesWebSocketHandler previewSocket,
                                        final WebSite previewWebSite) {

        this.previewSocket = previewSocket;
        this.previewWebSite = previewWebSite;
    }

    @Override
    public void onTocChange(Path tocPath) {
        ConsoleOutputs.out("toc changed: ", tocPath);
        execute(() -> {
            TocAddedUpdatedAndRemovedPages tocAddedUpdatedAndRemovedPages = previewWebSite.updateToc();
            previewSocket.sendToc(tocAddedUpdatedAndRemovedPages.tableOfContents());

            if (!tocAddedUpdatedAndRemovedPages.addedOrUpdatedPagesProps().isEmpty()) {
                previewSocket.sendPages(tocAddedUpdatedAndRemovedPages.addedOrUpdatedPagesProps().stream()
                        .map(HtmlPageAndPageProps::getProps));
            }

            if (!tocAddedUpdatedAndRemovedPages.removedTocItems().isEmpty()) {
                previewSocket.sendPagesRemove(tocAddedUpdatedAndRemovedPages.removedTocItems().stream());
            }
        });
    }

    @Override
    public void onFooterChange(Path tocPath) {
        ConsoleOutputs.out("footer changed: ", tocPath);
        execute(() -> {
            Footer footer = previewWebSite.updateFooter();
            previewSocket.sendFooter(footer);
        });
    }

    @Override
    public void onGlobalDocReferencesChange(Path docReferencePath) {
        ConsoleOutputs.out("global doc references changed: ", docReferencePath);

        execute(() -> {
            DocReferences docReferences = previewWebSite.updateDocReferences();
            previewSocket.sendDocReferences(docReferences);
        });
    }

    @Override
    public void onDocMetaChange(Path metaPath) {
        ConsoleOutputs.out("meta changed: ", metaPath);

        execute(() -> {
            String metaJson = FileUtils.fileTextContent(metaPath);
            DocMeta newDocMeta = previewWebSite.getDocMeta().cloneWithNewJson(metaJson);

            previewSocket.sendMeta(newDocMeta);
        });
    }

    private HtmlPageAndPageProps regenerate(Path markupPath) {
        final TocItem tocItem = previewWebSite.tocItemByPath(markupPath);

        if (tocItem == null) {
            return null;
        }

        return previewWebSite.regenerateAndValidatePageDeployTocAndAllPages(tocItem);
    }

    @Override
    public void onChange(Path path) {
        ConsoleOutputs.out("file changed: ", path);

        final TocItem tocItem = previewWebSite.tocItemByPath(path);
        if (tocItem == null) {
            ConsoleOutputs.out(path + " is not part of table of contents, checking dependent files");
            onDependentChange(path);
        } else {
            onMarkupChange(path);
        }
    }

    private void onMarkupChange(Path path) {
        execute(() -> {
            HtmlPageAndPageProps htmlPageAndPageProps = regenerate(path);

            if (htmlPageAndPageProps == null) {
                return;
            }

            previewSocket.sendPage(htmlPageAndPageProps.getProps());
            previewSocket.sendToc(previewWebSite.getToc());
        });
    }

    private void onDependentChange(Path path) {
        execute(() -> {
            Collection<TocItem> dependentTocItems = previewWebSite.dependentTocItems(path);
            if (dependentTocItems.isEmpty()) {
                ConsoleOutputs.out("no markup files depends on ", BLUE, path);
                return;
            }

            previewWebSite.redeployAuxiliaryFileIfRequired(path);

            dependentTocItems.forEach(System.out::println);
            Stream<DocPageReactProps> generatedPages = dependentTocItems.stream().
                    map(tocItem -> previewWebSite.regenerateAndValidatePageDeployTocAndAllPages(tocItem).getProps());

            previewSocket.sendPages(generatedPages);
            previewSocket.sendToc(previewWebSite.getToc());
        });
    }

    private void execute(Runnable code) {
        try {
            code.run();
        } catch (Exception e) {
            ConsoleOutputs.err(e.getMessage());
            previewSocket.sendError(e.getMessage(), renderStackTrace(e));
        }
    }

    private static String renderStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);

        return stringWriter.toString();
    }
}
