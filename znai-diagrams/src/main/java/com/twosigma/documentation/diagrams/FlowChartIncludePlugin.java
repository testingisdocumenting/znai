package com.twosigma.documentation.diagrams;

import com.twosigma.documentation.diagrams.graphviz.Graphviz;
import com.twosigma.documentation.diagrams.graphviz.GraphvizDiagram;
import com.twosigma.documentation.diagrams.graphviz.gen.DiagramNode;
import com.twosigma.documentation.diagrams.graphviz.gen.GraphvizFromJsonGen;
import com.twosigma.documentation.diagrams.graphviz.gen.GraphvizGenConfig;
import com.twosigma.documentation.diagrams.graphviz.gen.GraphvizGenResult;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.structure.DocStructure;
import com.twosigma.documentation.structure.DocUrl;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.documentation.diagrams.graphviz.GraphvizEngine.DOT_LAYOUT;
import static java.util.stream.Collectors.toList;

public class FlowChartIncludePlugin implements IncludePlugin {
    private static final AtomicInteger diagramCount = new AtomicInteger();

    private Path filePath;
    private List<Path> nodeLibPath;
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

        DiagramsGlobalAssetsRegistration.register(componentsRegistry.globalAssetsRegistry());

        String graphJson = componentsRegistry.resourceResolver().textContent(filePath);

        GraphvizGenConfig genConfig = new GraphvizGenConfig();
        genConfig.setVertical(pluginParams.getOpts().get("vertical", false));

        Map<String, ?> graph = JsonUtils.deserializeAsMap(graphJson);
        GraphvizGenResult genResult = new GraphvizFromJsonGen(graph,
                loadNodeLibraries(componentsRegistry.resourceResolver(), pluginParams),
                genConfig).generate();

        GraphvizDiagram diagram = Graphviz.graphvizEngine.diagramFromGv(
                pluginParams.getOpts().get("layout", DOT_LAYOUT),
                "dag" + diagramCount.incrementAndGet(),
                genResult.getGraphViz());

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("diagram", diagram.toMap());
        props.put("idsToHighlight", pluginParams.getOpts().getList("highlight"));
        props.put("wide", pluginParams.getOpts().get("wide", false));

        Map<String, String> urls = extractUrls(genResult.getUsedNodes());
        validateUrls(markupPath, urls);

        props.put("urls", convertUrls(urls));

        return PluginResult.docElement("GraphVizDiagram", props);
    }

    private Map<String, String> extractUrls(Collection<DiagramNode> nodes) {
        return nodes.stream()
                .filter(DiagramNode::hasUrl)
                .collect(Collectors.toMap(DiagramNode::getId, DiagramNode::getUrl));
    }

    private void validateUrls(Path markupPath, Map<String, String> urls) {
        urls.values().forEach(url -> docStructure.validateUrl(markupPath, "<flow-diagram>", new DocUrl(url)));
    }

    private Map<String, String> convertUrls(Map<String, String> urls) {
        return urls.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, this::buildUrl));
    }

    private List<List<?>> loadNodeLibraries(ResourcesResolver resourcesResolver,
                                            PluginParams pluginParams) {
        nodeLibPath = pluginParams.getOpts().getList("nodeLibPath").stream()
                .map(filePath -> resourcesResolver.fullPath(filePath.toString()))
                .collect(Collectors.toList());

        return nodeLibPath.stream()
                .map(resourcesResolver::textContent)
                .map(JsonUtils::deserializeAsList)
                .collect(toList());

    }

    private String buildUrl(Map.Entry<String, String> entry) {
        return docStructure.createUrl(new DocUrl(entry.getValue()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(filePath)),
                nodeLibPath.stream().map(AuxiliaryFile::builtTime));
    }
}
