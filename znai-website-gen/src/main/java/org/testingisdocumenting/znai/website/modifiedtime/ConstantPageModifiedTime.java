package org.testingisdocumenting.znai.website.modifiedtime;

import org.testingisdocumenting.znai.structure.TocItem;

import java.nio.file.Path;
import java.time.Instant;

public class ConstantPageModifiedTime implements PageModifiedTimeStrategy {
    private final Instant constantTime;

    public ConstantPageModifiedTime(Instant constantTime) {
        this.constantTime = constantTime;
    }

    @Override
    public Instant lastModifiedTime(TocItem tocItem, Path markupPath) {
        return constantTime;
    }
}
