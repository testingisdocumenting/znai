package com.twosigma.documentation.server.preview;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.documentation.WebSite;
import com.twosigma.documentation.html.HtmlPageAndPageProps;
import com.twosigma.documentation.server.preview.FileChangeHandler;
import com.twosigma.documentation.server.preview.PreviewFilesAssociationTracker;
import com.twosigma.documentation.server.preview.PreviewWebSocketHandler;
import com.twosigma.documentation.structure.TocItem;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class PreviewPushFileChangeHandler implements FileChangeHandler {
    private PreviewWebSocketHandler previewSocket;
    private WebSite previewWebSite;
    private PreviewFilesAssociationTracker filesAssociationTracker;

    public PreviewPushFileChangeHandler(final PreviewWebSocketHandler previewSocket,
        final WebSite previewWebSite,
        final PreviewFilesAssociationTracker filesAssociationTracker) {

        this.previewSocket = previewSocket;
        this.previewWebSite = previewWebSite;
        this.filesAssociationTracker = filesAssociationTracker;
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

        previewSocket.sendPageContent(htmlPageAndPageProps.getProps());
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

        Collection<Path> dependentMarkdowns = filesAssociationTracker.dependentMarkups(path);
        dependentMarkdowns.forEach(this::regenerate);

        final List<String> htmls = dependentMarkdowns.stream().map(p -> "/" + p.getParent().getFileName().toString() + "/" + p.getFileName()).
            map(s -> s.replaceAll("\\.md$", ".html")).
            collect(toList());

        if (! htmls.isEmpty()) {
            ConsoleOutputs.out("dependent htmls: " + htmls.stream().collect(Collectors.joining("\n")));
//            previewSocket.sendOpen(htmls.stream());
        }
    }
}
