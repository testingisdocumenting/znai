package com.twosigma.documentation.extensions.include;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class IncludeResult {
    private String html;
    private List<Path> usedFiles; // list of files that where used by the plugin. watcher can use that information
    private IncludeContext context;

    private IncludeResult() {
        usedFiles = new ArrayList<>();
    }

    public static IncludeResult inContext(IncludeContext context) {
        final IncludeResult result = new IncludeResult();
        result.context = context;

        return result;
    }

    public IncludeResult withHtml(String html) {
        this.html = html;
        return this;
    }

    public IncludeContext getContext() {
        return context;
    }

    public String getHtml() {
        return html;
    }

    public Stream<Path> getUsedFiles() {
        return usedFiles.stream();
    }

    public IncludeResult andUsedFile(Path path) {
        usedFiles.add(path);
        return this;
    }
}
