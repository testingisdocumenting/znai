package com.twosigma.documentation

import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine
import com.twosigma.documentation.nashorn.NashornEngine

/**
 * @author mykola
 */
class TestNashornEngine {
    static NashornEngine instance = create()

    private static NashornEngine create() {
        def engine = new ReactJsNashornEngine()
        engine.getNashornEngine().eval("toc = []")
        engine.loadCoreLibraries()

        return engine.nashornEngine
    }
}
