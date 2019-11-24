package com.twosigma.znai.website.modifiedtime;

import com.twosigma.znai.structure.TocItem;

import java.nio.file.Path;
import java.time.Instant;

public interface PageModifiedTimeStrategy {
    Instant lastModifiedTime(TocItem tocItem, Path markupPath);
}
