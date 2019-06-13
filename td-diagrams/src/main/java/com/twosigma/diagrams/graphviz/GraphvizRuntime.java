package com.twosigma.diagrams.graphviz;

/**
 * @author mykola
 */
public interface GraphvizRuntime {
    String svgFromGv(String gv);
    String getLayoutType();
}
