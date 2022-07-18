/*
 * Copyright 2022 znai maintainers
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
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ImageFencePlugin extends ImagePluginBase implements FencePlugin {
    private ComponentsRegistry componentsRegistry;
    private Path markupPath;
    private String content;

    @Override
    public FencePlugin create() {
        return new ImageFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;
        this.content = content;
        return process(componentsRegistry, markupPath, pluginParams);
    }

    @Override
    protected List<Map<String, Object>> annotationShapes(BufferedImage image) {
        return new CsvAnnotations(componentsRegistry.markdownParser(), markupPath, image, pixelRatio())
                .annotationsShapesFromCsv(content);
    }

    @Override
    protected Double pixelRatio() {
        if (pixelRatioFromOpts != null) {
            return pixelRatioFromOpts;
        }

        return 1.0;
    }

    @Override
    protected PluginParamsDefinition additionalParameters() {
        return PluginParamsDefinition.undefined();
    }

    @Override
    protected Stream<AuxiliaryFile> additionalAuxiliaryFiles() {
        return Stream.empty();
    }

}
