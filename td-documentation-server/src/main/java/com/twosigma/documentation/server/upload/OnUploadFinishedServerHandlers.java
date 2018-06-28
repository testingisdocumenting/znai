package com.twosigma.documentation.server.upload;

import com.twosigma.utils.ServiceLoaderUtils;
import io.vertx.core.Vertx;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author mykola
 */
public class OnUploadFinishedServerHandlers {
    private static final Set<OnUploadFinishedServerHandler> handlers =
            ServiceLoaderUtils.load(OnUploadFinishedServerHandler.class);

    public static void onUploadFinished(Vertx vertx, String docId, Path destination) {
        vertx.executeBlocking((req) -> handlers.forEach(h -> h.onUploadFinished(docId, destination)),
                (res) -> { });
    }

    public static void add(OnUploadFinishedServerHandler handler) {
        handlers.add(handler);
    }
}
