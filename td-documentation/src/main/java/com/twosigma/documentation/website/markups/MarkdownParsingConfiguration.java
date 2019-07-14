package com.twosigma.documentation.website.markups;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.structure.PlainTextTocGenerator;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;

import java.nio.file.Path;

import static com.twosigma.utils.FileUtils.fileTextContent;

public class MarkdownParsingConfiguration implements MarkupParsingConfiguration {
    @Override
    public TableOfContents createToc(Path tocPath) {
        TableOfContents toc = new PlainTextTocGenerator(filesExtension()).generate(fileTextContent(tocPath));
        toc.addIndex();

        return toc;
    }

    @Override
    public MarkupParser createMarkupParser(ComponentsRegistry componentsRegistry) {
        return new MarkdownParser(componentsRegistry);
    }

    @Override
    public String filesExtension() {
        return "md";
    }

    @Override
    public Path fullPath(Path root, TocItem tocItem) {
        return root.resolve(tocItem.getDirName()).resolve(tocItem.getFileNameWithoutExtension() + "." + filesExtension());
    }
}
