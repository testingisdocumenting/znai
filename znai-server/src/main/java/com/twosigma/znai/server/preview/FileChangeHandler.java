package com.twosigma.znai.server.preview;

import java.nio.file.Path;

public interface FileChangeHandler {
    void onTocChange(Path path);
    void onDocMetaChange(Path path);
    void onChange(Path path);
}