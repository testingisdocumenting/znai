package com.twosigma.documentation.extensions.image;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.structure.DocStructure;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ImageIncludePlugin implements IncludePlugin {
    private Path annotationsPath;
    private Path slidesPath;
    private ResourcesResolver resourceResolver;
    private AuxiliaryFile auxiliaryFile;

    @Override
    public String id() {
        return "image";
    }

    @Override
    public IncludePlugin create() {
        return new ImageIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        resourceResolver = componentsRegistry.resourceResolver();
        DocStructure docStructure = componentsRegistry.docStructure();
        String imagePath = pluginParams.getFreeParam();

        auxiliaryFile = resourceResolver.runtimeAuxiliaryFile(imagePath);

        String annotationsPathValue = pluginParams.getOpts().get("annotationsPath");
        String slidesPathValue = pluginParams.getOpts().get("slidesPath");
        Double scaleRatio = pluginParams.getOpts().get("scaleRatio", 1.0);

        annotationsPath = annotationsPathValue != null ? resourceResolver.fullPath(annotationsPathValue) : null;
        slidesPath = slidesPathValue != null ? resourceResolver.fullPath(slidesPathValue) : null;

        Map<String, ?> annotations = annotationsPath == null ? null : JsonUtils.deserializeAsMap(FileUtils.fileTextContent(annotationsPath));
        Map<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        props.put("imageSrc", docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString()));

        props.put("timestamp", System.currentTimeMillis());

        props.put("shapes", annotations != null ? annotations.get("shapes") : Collections.emptyList());
        setWidthHeight(props, scaleRatio, annotations, imagePath);

        return PluginResult.docElement("AnnotatedImage", props);
    }

    private void setWidthHeight(Map<String, Object> props,
                                Double scaleRatio,
                                Map<String, ?> annotations,
                                String imagePathValue) {
        Number pixelRatio = (annotations == null || !annotations.containsKey("pixelRatio")) ? 1 : (Number) annotations.get("pixelRatio");

        BufferedImage bufferedImage = resourceResolver.imageContent(imagePathValue);
        props.put("width", scaleRatio * bufferedImage.getWidth() / pixelRatio.doubleValue());
        props.put("height", scaleRatio * bufferedImage.getHeight() / pixelRatio.doubleValue());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(Stream.of(auxiliaryFile),
                annotationsPath != null ? Stream.of(AuxiliaryFile.builtTime(annotationsPath)) : Stream.empty());
    }
}
