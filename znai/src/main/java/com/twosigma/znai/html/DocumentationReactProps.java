package com.twosigma.znai.html;

import com.twosigma.znai.structure.DocMeta;

import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentationReactProps {
    private final DocMeta docMeta;
    private final DocPageReactProps pageProps;
    private FooterProps footerProps;

    public DocumentationReactProps(DocMeta docMeta, DocPageReactProps pageProps, FooterProps footerProps) {
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
