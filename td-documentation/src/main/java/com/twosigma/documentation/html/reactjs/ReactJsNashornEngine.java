package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.html.WebResource;
import com.twosigma.documentation.nashorn.NashornEngine;

import java.util.List;

import static com.twosigma.documentation.website.ProgressReporter.reportPhase;

/**
 * @author mykola
 */
public class ReactJsNashornEngine {
    private final ReactJsBundle reactJsBundle;
    private final NashornEngine nashornEngine;

    public ReactJsNashornEngine() {
        this.reactJsBundle = new ReactJsBundle();
        this.nashornEngine = new NashornEngine();

        initJsEngine();
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

    private void initJsEngine() {
        reportPhase("initializing ReactJS server side engine");

        nashornEngine.eval("toc = []");
        loadCoreLibraries();

        reportPhase("ReactJS initialized");
    }

    private void loadCoreLibraries() {
        reactJsBundle.serverJavaScripts().forEach(nashornEngine::loadLibrary);
    }
}
