package com.twosigma.documentation.website.markups;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;

import java.nio.file.Path;

public interface MarkupParsingConfiguration {
    TableOfContents createToc(Path tocPath);

    MarkupParser createMarkupParser(ComponentsRegistry componentsRegistry);

    String filesExtension();

    Path fullPath(Path root, TocItem tocItem);
}
