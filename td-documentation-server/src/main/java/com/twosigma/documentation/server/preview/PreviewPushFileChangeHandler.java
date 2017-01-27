package com.twosigma.documentation.server.preview;

import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Stream;

import com.twosigma.console.ConsoleOutput;
import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.html.HtmlPageAndPageProps;
import com.twosigma.documentation.html.PageProps;
import com.twosigma.documentation.structure.TocItem;

import static com.twosigma.console.ansi.Color.*;

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
    public void onTocChange(final Path path) {
        ConsoleOutputs.out("toc changed: ", path);
//        previewWebSite.regenerateToc();
//        previewSocket.sendOpen(Stream.of("/"));
    }

    @Override
    public void onMarkupChange(final Path path) {
        ConsoleOutputs.out("md changed: ", path);
        HtmlPageAndPageProps htmlPageAndPageProps = regenerate(path);

        if (htmlPageAndPageProps == null) {
            return;
        }

        previewSocket.sendPage(htmlPageAndPageProps.getProps());
    }

    private HtmlPageAndPageProps regenerate(final Path markupPath) {
        final TocItem tocItem = previewWebSite.tocItemByPath(markupPath);

        if (tocItem == null) {
            ConsoleOutputs.err(markupPath + " is not part of table of contents");
            return null;
        }

        return previewWebSite.regeneratePage(tocItem);
    }

    @Override
    public void onChange(final Path path) {
        ConsoleOutputs.out("file changed: ", path);

        Collection<TocItem> dependentTocItems = previewWebSite.dependentTocItems(path);
        if (dependentTocItems.isEmpty()) {
            ConsoleOutputs.out("no markup files depends on ", BLUE, path);
            return;
        }

        dependentTocItems.forEach(System.out::println);
        Stream<PageProps> generatedPages = dependentTocItems.stream().
                map(tocItem -> previewWebSite.regeneratePage(tocItem).getProps());

        previewSocket.sendPages(generatedPages);
    }
}
