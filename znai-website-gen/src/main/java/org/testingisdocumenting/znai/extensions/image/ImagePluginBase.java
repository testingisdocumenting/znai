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
import org.testingisdocumenting.znai.extensions.Plugin;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;
import org.testingisdocumenting.znai.utils.UrlUtils;

import javax.print.Doc;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

abstract class ImagePluginBase implements Plugin {
    protected AuxiliaryFile auxiliaryFile;
    protected boolean isExternal;

    private ResourcesResolver resourceResolver;

    @Override
    public String id() {
        return "image";
    }

    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        if (isExternal) {
            return additionalAuxiliaryFiles();
        }

        return Stream.concat(Stream.of(auxiliaryFile), additionalAuxiliaryFiles());
    }

    protected abstract List<Map<String, ?>> annotationShapes();

    protected abstract Double pixelRatio();

    protected abstract Stream<AuxiliaryFile> additionalAuxiliaryFiles();

    protected PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        resourceResolver = componentsRegistry.resourceResolver();
        DocStructure docStructure = componentsRegistry.docStructure();
        String imagePath = pluginParams.getFreeParam();

        isExternal = UrlUtils.isExternal(imagePath);
        PluginParamsOpts opts = pluginParams.getOpts();
        Map<String, Object> props = new LinkedHashMap<>(opts.toMap());

        if (isExternal) {
            props.put("imageSrc", imagePath);
            props.put("shapes", annotationShapes());
            docStructure.validateUrl(markupPath, "<image plugin>", new DocUrl(imagePath));
        } else {
            auxiliaryFile = resourceResolver.runtimeAuxiliaryFile(imagePath);
            Double scale = opts.get("scaleRatio", opts.get("scale", 1.0));
            props.put("imageSrc", docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString()));
            props.put("timestamp", componentsRegistry.timeService().fileModifiedTimeMillis(auxiliaryFile.getPath()));
            props.put("shapes", annotationShapes());
            setWidthHeight(props, scale, imagePath);
        }

        return PluginResult.docElement("AnnotatedImage", props);
    }

    private void setWidthHeight(Map<String, Object> props,
                                Double scale,
                                String imagePathValue) {
        Number pixelRatio = pixelRatio();

        BufferedImage bufferedImage = resourceResolver.imageContent(imagePathValue);
        props.put("width", scale * bufferedImage.getWidth() / pixelRatio.doubleValue());
        props.put("height", scale * bufferedImage.getHeight() / pixelRatio.doubleValue());
    }
}
