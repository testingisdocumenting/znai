package com.twosigma.documentation.server.upload;

import com.twosigma.utils.ServiceLoaderUtils;

import java.nio.file.Path;
import java.util.Set;

/**
 * @author mykola
 */
public class OnUploadFinishedServerHandlers {
    private static final Set<OnUploadFinishedServerHandler> handlers =
            ServiceLoaderUtils.load(OnUploadFinishedServerHandler.class);

    public static void onUploadFinished(String docId, Path destination) {
        handlers.forEach(h -> h.onUploadFinished(docId, destination));
    }

    public static void add(OnUploadFinishedServerHandler handler) {
        handlers.add(handler);
    }
}
