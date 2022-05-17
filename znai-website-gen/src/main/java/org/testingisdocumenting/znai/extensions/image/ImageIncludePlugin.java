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
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.utils.FilePathUtils;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ImageIncludePlugin extends ImagePluginBase implements IncludePlugin {
    private Map<String, ?> annotations;
    private ResourcesResolver resourceResolver;

    protected Path annotationsPath;

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
        String imagePath = pluginParams.getFreeParam();

        annotationsPath = determineAnnotationsPath(imagePath, pluginParams);
        annotations = this.annotationsPath == null ?
                null :
                JsonUtils.deserializeAsMap(FileUtils.fileTextContent(this.annotationsPath));

        return process(componentsRegistry, markupPath, pluginParams);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Map<String, ?>> annotationShapes() {
        return annotations != null ? (List<Map<String, ?>>) annotations.get("shapes") : Collections.emptyList();
    }

    @Override
    protected Double pixelRatio() {
        return (annotations == null || !annotations.containsKey("pixelRatio")) ?
                1.0 :
                ((Number) annotations.get("pixelRatio")).doubleValue();
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

        String annotationsPath = FilePathUtils.replaceExtension(imagePath, "json");
        if (!resourceResolver.canResolve(annotationsPath)) {
            throw new RuntimeException("can't find: " + annotationsPath);
        }

        return resourceResolver.fullPath(annotationsPath);
    }
}
