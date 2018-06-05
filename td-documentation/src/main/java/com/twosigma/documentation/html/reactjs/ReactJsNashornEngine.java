package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.nashorn.NashornEngine;
import com.twosigma.documentation.web.WebResource;

import java.util.stream.Stream;

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

    public void loadCustomLibraries(Stream<WebResource> jsResources) {
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
