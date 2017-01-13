package com.twosigma.documentation.html.reactjs;

import com.twosigma.documentation.nashorn.NashornEngine;

/**
 * @author mykola
 */
public class ReactJsNashornEngine {
    private final ReactJsBundle reactJsBundle;
    private final NashornEngine nashornEngine;

    public ReactJsNashornEngine() {
        this.reactJsBundle = new ReactJsBundle("bundle.txt");

         this.nashornEngine = new NashornEngine();

        nashornEngine.loadLibrary(reactJsBundle.react());
        nashornEngine.loadLibrary(reactJsBundle.reactDomServer());
        reactJsBundle.javaScriptResources().forEach(nashornEngine::loadLibrary);
    }

    public ReactJsBundle getReactJsBundle() {
        return reactJsBundle;
    }

    public NashornEngine getNashornEngine() {
        return nashornEngine;
    }
}
