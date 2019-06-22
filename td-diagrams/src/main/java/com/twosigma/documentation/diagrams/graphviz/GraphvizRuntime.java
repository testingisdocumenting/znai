package com.twosigma.documentation.diagrams.graphviz;

/**
 * @author mykola
 */
public interface GraphvizRuntime {
    String svgFromGv(String gv);
    String getLayoutType();
}
