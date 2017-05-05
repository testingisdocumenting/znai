package com.twosigma.documentation.extensions.image;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResourcesResolver;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class ImageIncludePlugin implements IncludePlugin {
    private Path imagePath;
    private Path annotationsPath;
    private Path slidesPath;
    private PluginResourcesResolver resourceResolver;

    @Override
    public String id() {
        return "image";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        resourceResolver = componentsRegistry.includeResourceResolver();
        String imagePathValue = pluginParams.getFreeParam();
        imagePath = resourceResolver.fullPath(imagePathValue);

        String annotationsPathValue = pluginParams.getOpts().get("annotationsPath");
        String slidesPathValue = pluginParams.getOpts().get("slidesPath");

        annotationsPath = annotationsPathValue != null ? resourceResolver.fullPath(annotationsPathValue) : null;
        slidesPath = slidesPathValue != null ? resourceResolver.fullPath(slidesPathValue) : null;

        Map<String, ?> annotations = annotationsPath == null ? null : JsonUtils.deserializeAsMap(FileUtils.fileTextContent(annotationsPath));
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("imageSrc", imagePathValue);
        props.put("shapes", annotations != null ? annotations.get("shapes") : Collections.emptyList());
        setWidthHeight(props, annotations, imagePathValue);

        return PluginResult.docElement("AnnotatedImage", props);
    }

    private void setWidthHeight(Map<String, Object> props, Map<String, ?> annotations, String imagePathValue) {
        Number pixelRatio = (annotations == null || !annotations.containsKey("pixelRatio")) ? 1 : (Number) annotations.get("pixelRatio");

        BufferedImage bufferedImage = resourceResolver.imageContent(imagePathValue);
        props.put("width", bufferedImage.getWidth() / pixelRatio.doubleValue());
        props.put("height", bufferedImage.getHeight() / pixelRatio.doubleValue());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(Stream.of(AuxiliaryFile.runTime(imagePath)), annotationsPath != null ?
                Stream.of(AuxiliaryFile.builtTime(annotationsPath)) : Stream.empty());
    }
}
