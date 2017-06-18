package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.html.HtmlPageAndPageProps;
import com.twosigma.documentation.html.PageProps;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

import static com.twosigma.console.ansi.Color.BLUE;

/**
 * @author mykola
 */
public class PreviewPushFileChangeHandler implements FileChangeHandler {
    private PreviewWebSocketHandler previewSocket;
    private WebSite previewWebSite;

    public PreviewPushFileChangeHandler(final PreviewWebSocketHandler previewSocket,
                                        final WebSite previewWebSite) {

        this.previewSocket = previewSocket;
        this.previewWebSite = previewWebSite;
    }

    @Override
    public void onTocChange(Path tocPath) {
        ConsoleOutputs.out("toc changed: ", tocPath);
        execute(() -> {
            TableOfContents toc = previewWebSite.updateToc();
            previewSocket.sendToc(toc);
            previewSocket.sendPages(toc.getTocItems().stream()
                    .map(previewWebSite::regeneratePage)
                    .map(HtmlPageAndPageProps::getProps));
        });
    }

    private HtmlPageAndPageProps regenerate(Path markupPath) {
        final TocItem tocItem = previewWebSite.tocItemByPath(markupPath);

        if (tocItem == null) {
            return null;
        }

        return previewWebSite.regeneratePage(tocItem);
    }

    @Override
    public void onChange(final Path path) {
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
            Stream<PageProps> generatedPages = dependentTocItems.stream().
                    map(tocItem -> previewWebSite.regeneratePage(tocItem).getProps());

            previewSocket.sendPages(generatedPages);
            previewSocket.sendToc(previewWebSite.getToc());
        });
    }

    private void execute(Runnable code) {
        try {
            code.run();
        } catch (Exception e) {
            ConsoleOutputs.err(e.getMessage());
            previewSocket.sendError(e.getMessage());
        }
    }
}
