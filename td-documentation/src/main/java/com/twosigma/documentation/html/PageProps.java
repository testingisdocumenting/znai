package com.twosigma.documentation.html;

import com.twosigma.documentation.parser.Page;
import com.twosigma.documentation.structure.DocMeta;
import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocItem;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * represents props for ReactJs Page component to render an entire documentation page
 * @author mykola
 */
public class PageProps {
    private final Map<String, ?> asMap;
    private final TocItem tocItem;
    private final Page page;
    private final HtmlRenderContext renderContext;

    public PageProps(final TocItem tocItem, final Page page, final HtmlRenderContext renderContext) {
        this.tocItem = tocItem;
        this.page = page;
        this.renderContext = renderContext;

        this.asMap = buildMap();
    }

    public Map<String, ?> toMap() {
        return asMap;
    }

    private Map<String, ?> buildMap() {
        final Map<String, ?> pageAsMap = page.getDocElement().toMap();
        Map<String, Object> pageProps = new LinkedHashMap<>();
//        pageProps.put("docMeta", docMeta.toMap());
//        pageProps.put("toc", toc.toListOfMaps());

        pageProps.put("type", "Page");
        pageProps.put("content", pageAsMap.get("content"));
        pageProps.put("tocItem", tocItem.toMap());
        pageProps.put("renderContext", renderContext.toMap());

        return pageProps;
    }
}
