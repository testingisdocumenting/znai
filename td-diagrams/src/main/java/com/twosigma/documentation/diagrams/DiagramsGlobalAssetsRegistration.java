package com.twosigma.documentation.diagrams;

import com.twosigma.documentation.core.GlobalAssetsRegistry;
import com.twosigma.documentation.diagrams.graphviz.Graphviz;

public class DiagramsGlobalAssetsRegistration {
    private DiagramsGlobalAssetsRegistration() {
    }

    public static void register(GlobalAssetsRegistry registry) {
        registry.updateAsset("graphvizDiagram", Graphviz.shapeConfig.getSvgByName());
    }
}
