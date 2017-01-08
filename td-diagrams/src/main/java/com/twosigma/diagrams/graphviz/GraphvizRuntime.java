package com.twosigma.diagrams.graphviz;

import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;

/**
 * @author mykola
 */
public interface GraphvizRuntime {
    String svgFromGv(String gv);
}
