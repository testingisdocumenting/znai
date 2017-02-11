package com.twosigma.documentation.server.preview;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.html.HtmlPageAndPageProps;
import com.twosigma.documentation.html.PageProps;
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
        execute(() -> previewSocket.sendToc(previewWebSite.updateToc()));
    }

    @Override
    public void onMarkupChange(Path path) {
        ConsoleOutputs.out("md changed: ", path);

        execute(() -> {
            HtmlPageAndPageProps htmlPageAndPageProps = regenerate(path);

            if (htmlPageAndPageProps == null) {
                return;
            }

            previewSocket.sendPage(htmlPageAndPageProps.getProps());
        });
    }

    private HtmlPageAndPageProps regenerate(Path markupPath) {
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

        execute(() -> {
            Collection<TocItem> dependentTocItems = previewWebSite.dependentTocItems(path);
            if (dependentTocItems.isEmpty()) {
                ConsoleOutputs.out("no markup files depends on ", BLUE, path);
                return;
            }

            dependentTocItems.forEach(System.out::println);
            Stream<PageProps> generatedPages = dependentTocItems.stream().
                    map(tocItem -> previewWebSite.regeneratePage(tocItem).getProps());

            previewSocket.sendPages(generatedPages);
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
