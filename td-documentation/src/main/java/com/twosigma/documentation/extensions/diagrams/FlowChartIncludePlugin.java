package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.diagrams.graphviz.gen.GraphvizFromJsonGen;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.structure.DocStructure;
import com.twosigma.documentation.structure.DocUrl;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class FlowChartIncludePlugin implements IncludePlugin {
    private static final AtomicInteger diagramCount = new AtomicInteger();

    private Path filePath;
    private DocStructure docStructure;

    @Override
    public String id() {
        return "flow-chart";
    }

    @Override
    public IncludePlugin create() {
        return new FlowChartIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        filePath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        docStructure = componentsRegistry.docStructure();

        String graphJson = componentsRegistry.resourceResolver().textContent(filePath);
        Map<String, ?> graph = JsonUtils.deserializeAsMap(graphJson);
        String gvContent = new GraphvizFromJsonGen(graph).generate();

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv("dag" + diagramCount.incrementAndGet(),
                gvContent);
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("colors", Graphviz.colors);
        props.put("idsToHighlight", pluginParams.getOpts().getList("highlight"));
        props.put("wide", pluginParams.getOpts().get("wide", false));
        props.put("urls", extractLinks(graph));

        return PluginResult.docElement("GraphVizDiagram", props);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> extractLinks(Map<String, ?> graph) {
        List<Map<String, ?>> nodes = (List<Map<String, ?>>) graph.get("nodes");

        return nodes.stream().filter(this::hasUrl)
                .collect(Collectors.toMap(n -> n.get("id").toString(),
                        this::buildUrl));
    }

    private boolean hasUrl(Map<String, ?> node) {
        return node.containsKey("url");
    }

    private String buildUrl(Map<String, ?> node) {
        return docStructure.createUrl(new DocUrl(node.get("url").toString()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(filePath));
    }
}
