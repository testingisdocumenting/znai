package com.twosigma.znai.diagrams;

import com.twosigma.znai.core.GlobalAssetsRegistry;
import com.twosigma.znai.diagrams.graphviz.Graphviz;

public class DiagramsGlobalAssetsRegistration {
    private DiagramsGlobalAssetsRegistration() {
    }

    public static void register(GlobalAssetsRegistry registry) {
        registry.updateAsset("graphvizDiagram", Graphviz.shapeConfig.getSvgByName());
    }
}
