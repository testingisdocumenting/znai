package com.twosigma.documentation.structure;

import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;

import com.twosigma.documentation.parser.PageSectionIdTitle;
import com.twosigma.documentation.parser.docelement.DocElement;

import static com.twosigma.documentation.parser.docelement.DocElementType.SECTION;
import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class Page {
    private final DocElement docElement;
    private final List<PageSectionIdTitle> pageSectionIdTitles;
    private final FileTime lastModifiedTime;
    private final PageMeta pageMeta;

    public Page(DocElement docElement, FileTime lastModifiedTime, PageMeta pageMeta) {
        this.docElement = docElement;
        this.pageSectionIdTitles = extractFirstLevelHeadings(docElement);
        this.lastModifiedTime = lastModifiedTime;
        this.pageMeta = pageMeta;
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public FileTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public List<PageSectionIdTitle> getPageSectionIdTitles() {
        return pageSectionIdTitles;
    }

    private List<PageSectionIdTitle> extractFirstLevelHeadings(final DocElement docElement) {
        return docElement.getContent().stream().
            filter(e -> e.getType().equals(SECTION)).
            map(this::createSectionIdTitle).
            collect(toList());
    }

    private PageSectionIdTitle createSectionIdTitle(DocElement docElement) {
        String title = docElement.getProp("title").toString();
        return new PageSectionIdTitle(title);
    }
}
