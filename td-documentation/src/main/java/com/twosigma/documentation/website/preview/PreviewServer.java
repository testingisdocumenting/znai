//package com.twosigma.documentation.website.preview;
//
//import java.nio.file.Path;
//
//import com.google.inject.servlet.ServletModule;
//
//import com.twosigma.cue.baseweb.BaseWebInit;
//import com.twosigma.cue.baseweb.BaseWebInit.BaseWebInitBuilder;
//import com.twosigma.cue.baseweb.ConfigurationPropertiesModule;
//import com.twosigma.cue.baseweb.ErrorPages;
//import com.twosigma.cue.baseweb.RootPathStaticResourceServlet;
//
///**
// * @author mykola
// */
//public class PreviewServer {
//    private BaseWebInitBuilder baseWebInitBuilder;
//    private PreviewSocket previewSocket;
//    private Path previewPath;
//
//    public PreviewServer(PreviewSocket previewSocket, Path previewPath) {
//        this.previewSocket = previewSocket;
//        this.previewPath = previewPath;
//
//        baseWebInitBuilder = BaseWebInit.with()
//            .resourceClasses()
//            .customModules(new PreviewModule())
//            .errorPages(ErrorPages.standard());
//    }
//
//    public void start() {
//        try {
//            baseWebInitBuilder.init();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private class PreviewModule extends ServletModule {
//        @Override
//        protected void configureServlets() {
//            serve("/preview").with(new PreviewSocketServlet(previewSocket));
//            serve("/*").with(
//                new RootPathStaticResourceServlet(previewPath.toAbsolutePath().toString()),
//                ConfigurationPropertiesModule.getServletCachingHeaderConfig());
//        }
//    }
//}
