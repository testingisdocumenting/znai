package com.twosigma.documentation.diagrams.graphviz.meta;

import com.twosigma.utils.RegexpUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class GraphvizDiagramWithMeta {
    private static final Pattern NODE_PATTERN =
            Pattern.compile("(\\S+)\\s*\\[.*?label\\s*=\\s*\"(.*?\\[[^]]*]\\s*)\".*?]\\s*;");

    private static final Pattern LABEL_PATTERN = Pattern.compile("\\[(.*?)]");

    private final String preprocessed;
    private final String[] contentLines;
    private final Map<String, List<String>> stylesById;
    private GraphvizShapeConfig metaConfig;

    public static GraphvizDiagramWithMeta create(GraphvizShapeConfig metaConfig, String diagramContent) {
        return new GraphvizDiagramWithMeta(metaConfig, diagramContent);
    }

    public String getPreprocessed() {
        return preprocessed;
    }

    public Map<String, List<String>> getStylesById() {
        return stylesById;
    }

    private GraphvizDiagramWithMeta(GraphvizShapeConfig metaConfig, String originalGv) {
        this.metaConfig = metaConfig;
        this.stylesById = new LinkedHashMap<>();
        this.contentLines = originalGv.split("\n");
        this.preprocessed = preprocess();
    }

    private String preprocess() {
        return Arrays.stream(contentLines).
                map((l) -> RegexpUtils.replaceAll(l, NODE_PATTERN, this::replaceAndExtractMeta)).
                collect(joining("\n"));
    }

    private String replaceAndExtractMeta(Matcher m) {
        String id = m.group(1);
        String label = m.group(2);

        List<String> styles = extractStyles(label);
        stylesById.put(id, styles);

        String additionalProps = additionalProps(styles);
        if (! additionalProps.isEmpty()) {
            additionalProps = "," + additionalProps;
        }

        return id + " [label=\"" + removeMeta(label) + "\"" + additionalProps + "];";
    }

    private String removeMeta(String label) {
        return RegexpUtils.replaceAll(label, LABEL_PATTERN, (m) -> "").trim();
    }

    private String additionalProps(List<String> styles) {
        return styles.stream().map(metaConfig::nodeShape).
                filter(Optional::isPresent).map(Optional::get).
                map(GraphvizNodeShape::asAttrs).
                findFirst().
                orElse("");

    }

    private List<String> extractStyles(String label) {
        Matcher matcher = LABEL_PATTERN.matcher(label);
        return matcher.find() ?
                Arrays.asList(matcher.group(1).split(" ")) : Collections.emptyList();
    }
}
