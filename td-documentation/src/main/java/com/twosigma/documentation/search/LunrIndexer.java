package com.twosigma.documentation.search;

import com.twosigma.documentation.html.DocPageReactProps;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.nashorn.NashornEngine;
import com.twosigma.utils.JsonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class LunrIndexer {
    private final NashornEngine nashornEngine;
    private final ReactJsNashornEngine reactJsNashornEngine;

    public LunrIndexer(ReactJsNashornEngine reactJsNashornEngine) {
        this.reactJsNashornEngine = reactJsNashornEngine;
        this.nashornEngine = this.reactJsNashornEngine.getNashornEngine();
    }

    public String createJsonIndex(Collection<DocPageReactProps> pages) {
        List<? extends Map<String, ?>> pagesAsMaps = pages.stream().map(DocPageReactProps::toMap).collect(toList());
        String pagesJson = JsonUtils.serialize(pagesAsMaps);
        nashornEngine.bind("pagesJson", pagesJson);
        return nashornEngine.eval("LunrIndexer.createWithPages(JSON.parse(pagesJson)).exportAsJson()").toString();
    }
}
