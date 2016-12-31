//package com.twosigma.documentation.website.preview;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.stream.Stream;
//
//import com.google.gson.Gson;
//
//import org.eclipse.jetty.websocket.WebSocket;
//
//import com.twosigma.cue.doc.gen.console.ConsoleOutput;
//import com.twosigma.cue.doc.gen.console.ConsoleOutputs;
//
//import static java.util.stream.Collectors.toList;
//
///**
// * High level overview of the problem domain blah blah
// *
// * @author mykola
// */
//public class PreviewSocket implements WebSocket.OnTextMessage, ConsoleOutput {
//    private Connection connection;
//
//    @Override
//    public void onMessage(final String message) {
//        System.out.println("message: " + message);
//    }
//
//    @Override
//    public void onOpen(final Connection connection) {
//        this.connection = connection;
//        this.connection.setMaxIdleTime(Integer.MAX_VALUE);
//        ConsoleOutputs.out("websocket connected");
//    }
//
//    public void sendReload() {
//        sendJson(Collections.singletonMap("type", "reload"));
//    }
//
//    public void sendOpen(Stream<String> paths) {
//        final LinkedHashMap<String, Object> command = new LinkedHashMap<>();
//        command.put("type", "open");
//        command.put("paths", paths.collect(toList()));
//
//        sendJson(command);
//    }
//
//    private void sendJson(Map<String, Object> command) {
//        final String payload = new Gson().toJson(command);
//        sendMessage(payload);
//    }
//
//    private void sendMessage(final String payload) {
//        if (connection == null) {
//            ConsoleOutputs.out("Connection with the preview page is lost. re-load page manually to reconnect");
//            return;
//        }
//
//        try {
//            this.connection.sendMessage(payload);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void onClose(final int i, final String s) {
//        connection = null;
//        ConsoleOutputs.out("websocket disconnected");
//    }
//
//    @Override
//    public void out(final Object... valueOrFormatting) {
//    }
//
//    @Override
//    public void err(final Object... valueOrFormatting) {
//        StringBuilder message = new StringBuilder();
//        Arrays.stream(valueOrFormatting).forEach(message::append);
//
//        final LinkedHashMap<String, Object> error = new LinkedHashMap<>();
//        error.put("type", "error");
//        error.put("message", message.toString());
//        sendJson(error);
//    }
//}
