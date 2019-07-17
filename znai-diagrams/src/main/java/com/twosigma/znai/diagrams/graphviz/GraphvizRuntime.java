package com.twosigma.znai.diagrams.graphviz;

public interface GraphvizRuntime {
    String svgFromGv(String gv);
    String getLayoutType();
}
