package com.twosigma.documentation.diagrams.graphviz;

public interface GraphvizRuntime {
    String svgFromGv(String gv);
    String getLayoutType();
}
