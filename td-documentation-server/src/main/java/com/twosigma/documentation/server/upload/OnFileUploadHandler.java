package com.twosigma.documentation.server.upload;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface OnFileUploadHandler {
    void onUploadFinished(Path destination);
}
