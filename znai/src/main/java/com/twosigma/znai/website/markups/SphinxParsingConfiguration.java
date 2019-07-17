package com.twosigma.znai.website.markups;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.sphinx.DocTreeTocGenerator;
import com.twosigma.znai.parser.sphinx.SphinxDocTreeParser;
import com.twosigma.znai.structure.TableOfContents;
import com.twosigma.znai.structure.TocItem;

import java.nio.file.Path;

import static com.twosigma.utils.FileUtils.fileTextContent;

public class SphinxParsingConfiguration implements MarkupParsingConfiguration {
    @Override
    public TableOfContents createToc(Path tocPath) {
        return new DocTreeTocGenerator(filesExtension()).generate(fileTextContent(tocPath));
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
