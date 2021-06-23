/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.extensions.image;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.utils.FilePathUtils;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;

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

        PluginParamsOpts opts = pluginParams.getOpts();

        String slidesPathValue = opts.get("slidesPath");
        Double scale = opts.get("scaleRatio", opts.get("scale", 1.0));

        annotationsPath = determineAnnotationsPath(imagePath, pluginParams);

        slidesPath = slidesPathValue != null ? resourceResolver.fullPath(slidesPathValue) : null;

        Map<String, ?> annotations = annotationsPath == null ? null : JsonUtils.deserializeAsMap(FileUtils.fileTextContent(annotationsPath));
        Map<String, Object> props = new LinkedHashMap<>(opts.toMap());
        props.put("imageSrc", docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString()));

        props.put("timestamp", componentsRegistry.timeService().fileModifiedTimeMillis(auxiliaryFile.getPath()));

        props.put("shapes", annotations != null ? annotations.get("shapes") : Collections.emptyList());
        setWidthHeight(props, scale, annotations, imagePath);

        return PluginResult.docElement("AnnotatedImage", props);
    }

    private void setWidthHeight(Map<String, Object> props,
                                Double scale,
                                Map<String, ?> annotations,
                                String imagePathValue) {
        Number pixelRatio = (annotations == null || !annotations.containsKey("pixelRatio")) ? 1 : (Number) annotations.get("pixelRatio");

        BufferedImage bufferedImage = resourceResolver.imageContent(imagePathValue);
        props.put("width", scale * bufferedImage.getWidth() / pixelRatio.doubleValue());
        props.put("height", scale * bufferedImage.getHeight() / pixelRatio.doubleValue());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(Stream.of(auxiliaryFile),
                annotationsPath != null ? Stream.of(AuxiliaryFile.builtTime(annotationsPath)) : Stream.empty());
    }

    private Path determineAnnotationsPath(String imagePath, PluginParams pluginParams) {
        String annotationsPathValue = pluginParams.getOpts().get("annotationsPath");
        if (annotationsPathValue != null) {
            return resourceResolver.fullPath(annotationsPathValue);
        }

        if (!pluginParams.getOpts().get("annotate", false)) {
            return null;
        }

        String annotationsPath = FilePathUtils.replaceExtension(imagePath, "json");
        if (!resourceResolver.canResolve(annotationsPath)) {
            throw new RuntimeException("can't find: " + annotationsPath);
        }

        return resourceResolver.fullPath(annotationsPath);
    }
}
