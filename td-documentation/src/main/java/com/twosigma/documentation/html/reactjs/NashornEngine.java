package com.twosigma.documentation.html.reactjs;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.twosigma.documentation.html.WebResource;

/**
 * @author mykola
 */
public class NashornEngine {
    private ScriptEngine engine;

    public NashornEngine() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        polyfillEngine();
    }

    public Object eval(String script) {
        try {
            return engine.eval(script);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadLibraries(Iterable<WebResource> libraries) {
        libraries.forEach(this::loadLibrary);
    }

    public void loadLibrary(WebResource library) {
        try {
            engine.eval(library.getContent());
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private void polyfillEngine() {
        eval("var global = this;\n" +
                "var window = this;\n" +
                "var console = {};\n" +
                "console.debug = print;\n" +
                "console.error = print;\n" +
                "console.warn = print;\n" +
                "console.log = print;");
    }

}
