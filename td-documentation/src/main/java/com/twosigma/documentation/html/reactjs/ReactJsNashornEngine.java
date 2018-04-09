package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.WebResource;
import com.twosigma.documentation.nashorn.NashornEngine;

import java.util.List;

/**
 * @author mykola
 */
public class ReactJsNashornEngine {
    private final ReactJsBundle reactJsBundle;
    private final NashornEngine nashornEngine;

    public ReactJsNashornEngine() {
        this.reactJsBundle = new ReactJsBundle();
        this.nashornEngine = new NashornEngine();
    }

    public void loadCoreLibraries() {
        reactJsBundle.serverJavaScripts().forEach(nashornEngine::loadLibrary);
    }

    public void loadCustomLibraries(List<WebResource> jsResources) {
        jsResources.forEach(nashornEngine::loadLibrary);
    }

    public ReactJsBundle getReactJsBundle() {
        return reactJsBundle;
    }

    public NashornEngine getNashornEngine() {
        return nashornEngine;
    }
}
