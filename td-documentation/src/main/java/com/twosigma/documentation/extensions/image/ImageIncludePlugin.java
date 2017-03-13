package com.twosigma.documentation.extensions.image;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResourcesResolver;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.diagrams.Graphviz;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class ImageIncludePlugin implements IncludePlugin {
    private Path imagePath;
    private Path annotationsPath;
    private Path slidesPath;

    @Override
    public String id() {
        return "image";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        PluginResourcesResolver resourceResolver = componentsRegistry.includeResourceResolver();
        String imagePathValue = includeParams.getFreeParam();
        imagePath = resourceResolver.fullPath(imagePathValue);

        String annotationsPathValue = includeParams.getOpts().get("annotationsPath");
        String slidesPathValue = includeParams.getOpts().get("slidesPath");

        annotationsPath = annotationsPathValue != null ? resourceResolver.fullPath(annotationsPathValue) : null;
        slidesPath = slidesPathValue != null ? resourceResolver.fullPath(slidesPathValue) : null;

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("imageSrc", imagePathValue);
        props.put("shapes", loadShapes(annotationsPath));

        return PluginResult.reactComponent("DocumentationAnnotatedImage", props);
    }

    private List<?> loadShapes(Path path) {
        return path == null ? Collections.emptyList() : JsonUtils.deserializeAsList(FileUtils.fileTextContent(path));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.runTime(imagePath), AuxiliaryFile.builtTime(annotationsPath));
    }
}
