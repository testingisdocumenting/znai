package com.twosigma.documentation.server.upload;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface OnUploadFinishedServerHandler {
    void onUploadFinished(String docId, Path destination);
}
