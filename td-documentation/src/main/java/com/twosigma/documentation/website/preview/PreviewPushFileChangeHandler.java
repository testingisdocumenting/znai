//package com.twosigma.documentation.website.preview;
//
//import java.nio.file.Path;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import com.twosigma.documentation.WebSite;
//import com.twosigma.cue.doc.gen.console.ConsoleOutputs;
//import com.twosigma.documentation.structure.TocItem;
//
//import static java.util.stream.Collectors.toList;
//
///**
// * @author mykola
// */
//public class PreviewPushFileChangeHandler implements FileChangeHandler {
//    private PreviewSocket previewSocket;
//    private WebSite previewWebSite;
//    private PreviewFilesAssociationTracker filesAssociationTracker;
//
//    public PreviewPushFileChangeHandler(final PreviewSocket previewSocket,
//        final WebSite previewWebSite,
//        final PreviewFilesAssociationTracker filesAssociationTracker) {
//
//        this.previewSocket = previewSocket;
//        this.previewWebSite = previewWebSite;
//        this.filesAssociationTracker = filesAssociationTracker;
//    }
//
//    @Override
//    public void onTocChange(final Path path) {
//        ConsoleOutputs.out("toc changed: ", path);
//        previewWebSite.regenerateToc();
//        previewSocket.sendOpen(Stream.of("/"));
//    }
//
//    @Override
//    public void onMdChange(final Path path) {
//        ConsoleOutputs.out("md changed: ", path);
//        final TocItem tocItem = regenerate(path);
//        if (tocItem == null) {
//            return;
//        }
//        previewSocket.sendOpen(Stream.of("/" + tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension() + ".html"));
//    }
//
//    private TocItem regenerate(final Path mdPath) {
//        final TocItem tocItem = previewWebSite.tocItemByPath(mdPath);
//
//        if (tocItem == null) {
//            ConsoleOutputs.err(mdPath + " is not part of table of contents");
//            return null;
//        }
//
//        previewWebSite.regeneratePage(tocItem);
//        return tocItem;
//    }
//
//    @Override
//    public void onChange(final Path path) {
//        ConsoleOutputs.out("file changed: ", path);
//
//        Collection<Path> dependentMarkdowns = filesAssociationTracker.dependentMarkdowns(path);
//        dependentMarkdowns.forEach(this::regenerate);
//
//        final List<String> htmls = dependentMarkdowns.stream().map(p -> "/" + p.getParent().getFileName().toString() + "/" + p.getFileName()).
//            map(s -> s.replaceAll("\\.md$", ".html")).
//            collect(toList());
//
//        if (! htmls.isEmpty()) {
//            ConsoleOutputs.out("dependent htmls: " + htmls.stream().collect(Collectors.joining("\n")));
//            previewSocket.sendOpen(htmls.stream());
//        }
//    }
//}
