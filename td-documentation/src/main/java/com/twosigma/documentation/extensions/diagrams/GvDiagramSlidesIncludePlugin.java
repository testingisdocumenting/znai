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

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class GvDiagramSlidesIncludePlugin implements IncludePlugin {
    private List<Path> filesMarkupDependsOn;
    private Path diagramPath;
    private Path slidesPath;

    @Override
    public String id() {
        return "gv-diagram-slides";
    }

    @Override
    public void reset(IncludeContext context) {

    }

    @Override
    public ReactComponent process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        MarkupParser parser = componentsRegistry.parser();
        IncludeResourcesResolver includeResourcesResolver = componentsRegistry.includeResourceResolver();

        String diagramTitle = includeParams.getFreeParam();
        String diagramId = NameUtils.camelCaseWithSpacesToDashes(diagramTitle);

        diagramPath = includeResourcesResolver.fullPath(includeParams.getOpts().getRequiredString("diagramPath"));
        slidesPath = includeResourcesResolver.fullPath(includeParams.getOpts().getRequiredString("slidesPath"));

        String gvContent = includeResourcesResolver.textContent(diagramPath);
        String slidesContent = includeResourcesResolver.textContent(slidesPath);

        MarkupDiagramSlides markupSlides = new MarkupDiagramSlides(parser);
        DiagramSlides diagramSlides = markupSlides.create(markupPath, slidesContent);

        filesMarkupDependsOn = markupSlides.getFilesMarkupDependsOn();

        Map<String, Object> props = new LinkedHashMap<>();

        props.put("slides", diagramSlides.toListOfMaps());
        props.put("diagram", Graphviz.graphvizEngine.diagramFromGv(diagramId, gvContent).toMap());
        props.put("colors", Graphviz.colors);

        return new ReactComponent("GraphVizFlow", props);
    }

    @Override
    public Stream<Path> filesPluginDependsOn(ComponentsRegistry componentsRegistry, IncludeParams includeParams) {
        return Stream.concat(filesMarkupDependsOn.stream(), Stream.of(diagramPath, slidesPath));
    }

    @Override
    public String textForSearch() {
        return null;
    }
}
