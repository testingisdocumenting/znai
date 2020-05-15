package org.testingisdocumenting.znai.website.modifiedtime;

import org.testingisdocumenting.znai.structure.TocItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;

public class FileBasedPageModifiedTime implements PageModifiedTimeStrategy {
    @Override
    public Instant lastModifiedTime(TocItem tocItem, Path markupPath) {
        try {
            FileTime fileTime = Files.getLastModifiedTime(markupPath);
            return fileTime.toInstant();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
