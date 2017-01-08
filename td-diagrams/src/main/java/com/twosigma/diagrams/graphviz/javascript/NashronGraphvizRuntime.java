package com.twosigma.diagrams.graphviz.javascript;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.diagrams.graphviz.GraphvizRuntime;
import com.twosigma.diagrams.graphviz.meta.GraphvizDiagramWithMeta;
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;

/**
 * @author mykola
 */
public class NashronGraphvizRuntime implements GraphvizRuntime {
    private NashornGraphviz nashornGraphviz;

    public NashronGraphvizRuntime() {
        nashornGraphviz = new NashornGraphviz();
    }

    @Override
    public String svgFromGv(String gv) {
        return nashornGraphviz.svgFromGv(gv);
    }
}
