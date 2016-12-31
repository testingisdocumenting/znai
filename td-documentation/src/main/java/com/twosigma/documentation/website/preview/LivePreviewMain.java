//package com.twosigma.documentation.website.preview;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import com.twosigma.base.init.BaseInit;
//import com.twosigma.base.util.TSProperties;
//import com.twosigma.documentation.WebSite;
//import com.twosigma.cue.doc.gen.console.ConsoleOutputs;
//import com.twosigma.cue.doc.gen.console.StandardConsoleOutput;
//import com.twosigma.documentation.html.WebResource;
//
///**
// * @author mykola
// */
//public class LivePreviewMain {
//    public static void main(String[] args) {
//        BaseInit.init();
//
//        final PreviewSocket previewSocket = new PreviewSocket();
//
//        ConsoleOutputs.add(new StandardConsoleOutput());
//        ConsoleOutputs.add(previewSocket);
//
//        final Path previewPath = Paths.get("__preview");
//        final Path bootstrapPath = Paths.get(TSProperties.getStringProperty("bootstrap.path"));
//        final Path webResourcesPath = Paths.get(TSProperties.getStringProperty("resources.path"));
//
//        final PreviewFilesAssociationTracker filesAssociationTracker = new PreviewFilesAssociationTracker();
//
//        final WebSite webSite = WebSite.withToc(Paths.get("toc")).
//            withWebResources(bootstrapPath, webResourcesPath).
//            withExtraJavaScripts(WebResource.withRelativePath("js/preview.js")).
//            withLogoRelativePath("img/two-sigma-logo.png").
//            withPluginListener(filesAssociationTracker).
//            withMetaFromJsonFile(Paths.get("meta.json")).deployTo(previewPath);
//
//        final PreviewPushFileChangeHandler fileChangeHandler = new PreviewPushFileChangeHandler(previewSocket, webSite, filesAssociationTracker);
//
//        final PreviewServer previewServer = new PreviewServer(previewSocket, previewPath);
//        previewServer.start();
//
//        final FileWatcher fileWatcher = new FileWatcher(fileChangeHandler);
//        fileWatcher.start();
//    }
//}
