//package com.twosigma.documentation.website.preview;
//
//import javax.inject.Singleton;
//import javax.servlet.http.HttpServletRequest;
//
//import org.eclipse.jetty.websocket.WebSocket;
//import org.eclipse.jetty.websocket.WebSocketServlet;
//
///**
// * @author mykola
// */
//@Singleton
//public class PreviewSocketServlet extends WebSocketServlet {
//    private PreviewSocket previewSocket;
//
//    public PreviewSocketServlet(PreviewSocket previewSocket) {
//        this.previewSocket = previewSocket;
//    }
//
//    @Override
//    public WebSocket doWebSocketConnect(final HttpServletRequest httpServletRequest, final String s) {
//        return previewSocket;
//    }
//}
