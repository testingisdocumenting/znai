package com.twosigma.documentation.html;

import com.twosigma.documentation.structure.Page;
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

    public PageProps(final TocItem tocItem, final Page page) {
        this.tocItem = tocItem;
        this.page = page;
        this.asMap = buildMap();
    }

    public Map<String, ?> toMap() {
        return asMap;
    }

    private Map<String, ?> buildMap() {
        Map<String, Object> pageProps = new LinkedHashMap<>();

        pageProps.put("type", "Page");
        pageProps.put("content", ((Map<String, ?>) page.getDocElement().toMap()).get("content"));
        pageProps.put("tocItem", tocItem.toMap());

        return pageProps;
    }
}
