package com.twosigma.documentation.nashorn;

import javax.script.*;

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

    public void bind(String varName, Object value) {
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put(varName, value);
    }

    public void loadLibraries(Iterable<WebResource> libraries) {
        libraries.forEach(this::loadLibrary);
    }

    public void loadLibrary(WebResource library) {
        try {
            engine.eval(library.getTextContent());
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
