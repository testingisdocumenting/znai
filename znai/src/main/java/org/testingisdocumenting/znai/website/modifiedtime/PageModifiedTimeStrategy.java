package org.testingisdocumenting.znai.website.modifiedtime;

import org.testingisdocumenting.znai.structure.TocItem;

import java.nio.file.Path;
import java.time.Instant;

public interface PageModifiedTimeStrategy {
    Instant lastModifiedTime(TocItem tocItem, Path markupPath);
}
