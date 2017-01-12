package com.twosigma.documentation.html;

import com.twosigma.documentation.parser.Page;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class DocumentationProps {
    private final DocMeta docMeta;
    private final TableOfContents toc;
    private final PageProps pageToRender;

    public DocumentationProps(DocMeta docMeta, TableOfContents toc, PageProps pageToRender) {
        this.docMeta = docMeta;
        this.toc = toc;
        this.pageToRender = pageToRender;
    }

    public Map<String, ?> toMap() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("docMeta", docMeta.toMap());
        map.put("toc", toc.toListOfMaps());
        map.put("page", pageToRender.toMap());

        return map;
    }
}
