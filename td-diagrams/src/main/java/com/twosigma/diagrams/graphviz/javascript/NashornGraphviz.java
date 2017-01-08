package com.twosigma.diagrams.graphviz.javascript;

import com.twosigma.utils.ResourceUtils;

import javax.script.*;

/**
 * @author mykola
 */
class NashornGraphviz {
    private final ScriptEngine engine;

    public NashornGraphviz() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(ResourceUtils.textContent("viz.js"));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public String svgFromGv(String gv) {
        try {
            Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
            bindings.put("graph", gv);
            return engine.eval("Viz(graph, {format: 'svg'})").toString();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
