package com.twosigma.znai.website.markups;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.structure.TableOfContents;
import com.twosigma.znai.structure.TocItem;

import java.nio.file.Path;

public interface MarkupParsingConfiguration {
    TableOfContents createToc(Path tocPath);

    MarkupParser createMarkupParser(ComponentsRegistry componentsRegistry);

    String filesExtension();

    Path fullPath(Path root, TocItem tocItem);
}
