package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.diagrams.slides.DiagramSlides;
import com.twosigma.documentation.extensions.include.*;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.utils.NameUtils;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class GvDiagramSlidesIncludePlugin implements IncludePlugin {
    private List<AuxiliaryFile> auxiliaryFiles;
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
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        MarkupParser parser = componentsRegistry.defaultParser();
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();

        String diagramTitle = pluginParams.getFreeParam();
        String diagramId = NameUtils.camelCaseWithSpacesToDashes(diagramTitle);

        diagramPath = resourcesResolver.fullPath(pluginParams.getOpts().getRequiredString("diagramPath"));
        slidesPath = resourcesResolver.fullPath(pluginParams.getOpts().getRequiredString("slidesPath"));

        String gvContent = resourcesResolver.textContent(diagramPath);
        String slidesContent = resourcesResolver.textContent(slidesPath);

        MarkupDiagramSlides markupSlides = new MarkupDiagramSlides(parser);
        DiagramSlides diagramSlides = markupSlides.create(markupPath, slidesContent);

        auxiliaryFiles = markupSlides.getAuxiliaryFiles();

        Map<String, Object> props = new LinkedHashMap<>();

        props.put("slides", diagramSlides.toListOfMaps());
        props.put("diagram", Graphviz.graphvizEngine.diagramFromGv(diagramId, gvContent).toMap());
        props.put("colors", Graphviz.colors);

        return PluginResult.docElement("GraphVizFlow", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(auxiliaryFiles.stream(), Stream.of(
                AuxiliaryFile.builtTime(diagramPath),
                AuxiliaryFile.builtTime(slidesPath)));
    }
}
