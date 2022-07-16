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
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.utils.FilePathUtils;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ImageIncludePlugin extends ImagePluginBase implements IncludePlugin {
    private static final String ANNOTATIONS_PATH_KEY = "annotationsPath";
    private static final String ANNOTATE_KEY = "annotate";

    private Map<String, ?> parsedJson;
    private ResourcesResolver resourceResolver;
    private ComponentsRegistry componentsRegistry;
    private Path markupPath;

    protected Path annotationsPath;
    private boolean isJsonFile;
    private String annotationsFileContent;

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
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;

        String imagePath = pluginParams.getFreeParam();

        annotationsPath = determineAnnotationsPath(imagePath, pluginParams);
        preparseJsonFileIfJson();

        return process(componentsRegistry, markupPath, pluginParams);
    }

    private void preparseJsonFileIfJson() {
        // we need to parse JSON earlier to get pixelRatio if available

        if (annotationsPath == null) {
            return;
        }

        annotationsFileContent = FileUtils.fileTextContent(this.annotationsPath);
        isJsonFile = annotationsFileContent.trim().startsWith("{");

        if (isJsonFile) {
            parsedJson = JsonUtils.deserializeAsMap(annotationsFileContent);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Map<String, ?>> annotationShapes(BufferedImage image) {
        if (annotationsPath == null) {
            return Collections.emptyList();
        }

        if (isJsonFile) {
            return (List<Map<String, ?>>) parsedJson.get("shapes");
        }

        return new CsvAnnotations(componentsRegistry.markdownParser(), markupPath, image, pixelRatio())
                .annotationsShapesFromCsv(annotationsFileContent);
    }

    @Override
    protected Double pixelRatio() {
        if (pixelRatioFromOpts != null) {
            return pixelRatioFromOpts;
        }

        return (parsedJson == null || !parsedJson.containsKey("pixelRatio")) ?
                1.0 :
                ((Number) parsedJson.get("pixelRatio")).doubleValue();
    }

    @Override
    protected PluginParamsDefinition additionalParameters() {
        return new PluginParamsDefinition()
                .add(ANNOTATIONS_PATH_KEY, PluginParamType.STRING,
                        "path to a JSON file with annotations information", "annotations.json")
                .add(ANNOTATE_KEY, PluginParamType.BOOLEAN,
                        "automatically use the annotations file matching image file but with json extension", "true");
    }

    @Override
    protected Stream<AuxiliaryFile> additionalAuxiliaryFiles() {
        return annotationsPath != null ? Stream.of(AuxiliaryFile.builtTime(annotationsPath)) : Stream.empty();
    }

    protected Path determineAnnotationsPath(String imagePath, PluginParams pluginParams) {
        String annotationsPathValue = pluginParams.getOpts().get("annotationsPath");
        if (annotationsPathValue != null) {
            return resourceResolver.fullPath(annotationsPathValue);
        }

        if (!pluginParams.getOpts().get("annotate", false)) {
            return null;
        }

        if (isExternal) {
            return null;
        }

        String jsonAnnotationsPath = FilePathUtils.replaceExtension(imagePath, "json");
        if (resourceResolver.canResolve(jsonAnnotationsPath)) {
            return resourceResolver.fullPath(jsonAnnotationsPath);
        }

        String csvAnnotationsPath = FilePathUtils.replaceExtension(imagePath, "csv");
        if (resourceResolver.canResolve(csvAnnotationsPath)) {
            return resourceResolver.fullPath(csvAnnotationsPath);
        }

        throw new RuntimeException("can't find any of the files: " + jsonAnnotationsPath + "; " + csvAnnotationsPath);
    }
}
