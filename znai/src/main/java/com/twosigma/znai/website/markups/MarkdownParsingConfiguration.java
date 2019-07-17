package com.twosigma.znai.website.markups;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.parser.commonmark.MarkdownParser;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.structure.PlainTextTocGenerator;
import com.twosigma.znai.structure.TableOfContents;
import com.twosigma.znai.structure.TocItem;

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
