package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.diagrams.graphviz.GraphvizEngine;
import com.twosigma.diagrams.graphviz.InteractiveCmdGraphviz;
import com.twosigma.diagrams.graphviz.meta.GraphvizShapeConfig;
import com.twosigma.documentation.ComponentsRegistry;
import com.twosigma.documentation.extensions.ReactComponent;
import com.twosigma.documentation.extensions.diagrams.slides.DiagramSlides;
import com.twosigma.documentation.extensions.include.IncludeContext;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludeResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.utils.NameUtils;
import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.ResourceUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class GvDiagramSlidesIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "gv-diagram-slides";
    }

    @Override
    public void reset(IncludeContext context) {

    }

    @Override
    public ReactComponent process(ComponentsRegistry componentsRegistry, IncludeParams includeParams) {
        MarkupParser parser = componentsRegistry.parser();
        IncludeResourcesResolver includeResourcesResolver = componentsRegistry.includeResourceResolver();

        String diagramTitle = includeParams.getFreeParam();
        String diagramId = NameUtils.camelCaseWithSpacesToDashes(diagramTitle);

        String gvContent = includeResourcesResolver.textContent(includeParams.getOpts().getRequiredString("diagramPath"));
        String slidesContent = includeResourcesResolver.textContent(includeParams.getOpts().getRequiredString("slidesPath"));

        DiagramSlides diagramSlides = new MarkupDiagramSlides(parser).create(slidesContent);

        Map<String, Object> props = new LinkedHashMap<>();

        props.put("slides", diagramSlides.toListOfMaps());
        props.put("diagram", Graphviz.graphvizEngine.diagramFromGv(diagramId, gvContent).toMap());
        props.put("colors", Graphviz.colors);

        return new ReactComponent("GraphVizFlow", props);
    }

    @Override
    public String textForSearch() {
        return null;
    }
}
