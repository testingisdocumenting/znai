package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.diagrams.graphviz.gen.GraphvizFromJsonGen;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class FlowChartIncludePlugin implements IncludePlugin {
    private static final AtomicInteger diagramCount = new AtomicInteger();

    private Path filePath;

    @Override
    public String id() {
        return "flow-chart";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        filePath = componentsRegistry.includeResourceResolver().fullPath(pluginParams.getFreeParam());

        String gvContent = new GraphvizFromJsonGen(
                componentsRegistry.includeResourceResolver().textContent(filePath)).generate();

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv("dag" + diagramCount.incrementAndGet(),
                gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("colors", Graphviz.colors);
        props.put("idsToHighlight", pluginParams.getOpts().getList("highlight"));
        props.put("wide", pluginParams.getOpts().get("wide", false));

        return PluginResult.docElement("GraphVizDiagram", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(filePath));
    }
}
