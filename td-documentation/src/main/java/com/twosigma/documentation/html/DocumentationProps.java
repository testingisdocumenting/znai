package com.twosigma.documentation.html;

import com.twosigma.documentation.structure.DocMeta;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class DocumentationProps {
    private final DocMeta docMeta;
    private final PageProps pageProps;
    private FooterProps footerProps;

    public DocumentationProps(DocMeta docMeta, PageProps pageProps, FooterProps footerProps) {
        this.docMeta = docMeta;
        this.pageProps = pageProps;
        this.footerProps = footerProps;
    }

    public Map<String, ?> toMap() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("docMeta", docMeta.toMap());
        map.put("page", pageProps.toMap());
        map.put("footer", footerProps.toMap());

        return map;
    }
}
