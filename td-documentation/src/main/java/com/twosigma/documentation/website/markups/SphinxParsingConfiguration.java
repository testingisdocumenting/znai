package com.twosigma.documentation.website.markups;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.sphinx.DocTreeTocGenerator;
import com.twosigma.documentation.parser.sphinx.SphinxDocTreeParser;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;

import java.nio.file.Path;

import static com.twosigma.utils.FileUtils.fileTextContent;

/**
 * @author mykola
 */
public class SphinxParsingConfiguration implements MarkupParsingConfiguration {
    @Override
    public TableOfContents createToc(Path tocPath) {
        return new DocTreeTocGenerator().generate(fileTextContent(tocPath));
    }

    @Override
    public MarkupParser createMarkupParser(ComponentsRegistry componentsRegistry) {
        return new SphinxDocTreeParser(componentsRegistry);
    }

    @Override
    public String filesExtension() {
        return "xml";
    }

    @Override
    public Path fullPath(Path root, TocItem tocItem) {
        return root.resolve(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension() + "." + filesExtension());
    }
}
