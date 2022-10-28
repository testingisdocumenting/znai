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
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.extensions.file.AnchorFeature;
import org.testingisdocumenting.znai.extensions.paramtypes.PluginParamTypeEnum;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

abstract class ImagePluginBase implements Plugin {
    private static final List<String> ALIGN_VALUES = Arrays.asList("left", "center", "right");

    protected static final String ALIGN_KEY = "align";
    protected static final String BORDER_KEY = "border";
    protected static final String TITLE_KEY = "title";
    protected static final String CAPTION_KEY = "caption";
    protected static final String CAPTION_BOTTOM_KEY = "captionBottom";
    protected static final String FIT_KEY = "fit";
    protected static final String SCALE_KEY = "scale";
    protected static final String SCALE_DEPRECATED_KEY = "scaleRatio";

    private static final String PIXEL_RATIO_KEY = "pixelRatio";

    private static final String MOBILE_ONLY_KEY = "mobileOnly";
    private static final String DESKTOP_ONLY_KEY = "desktopOnly";

    protected AuxiliaryFile auxiliaryFile;
    protected boolean isExternal;

    protected Double pixelRatioFromOpts;

    @Override
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition params = new PluginParamsDefinition();
        params.add(PluginParamsDefinitionCommon.container);
        // TODO use title, deprecate caption
        params.add(CAPTION_KEY, PluginParamType.STRING, "image title", "\"my image\"");
        // TODO deprecate
        params.add(CAPTION_BOTTOM_KEY, PluginParamType.BOOLEAN, "place image title at the bottom", "true");
        params.add(ALIGN_KEY, new PluginParamTypeEnum(ALIGN_VALUES), "horizontal image alignment");

        params.add(MOBILE_ONLY_KEY, PluginParamType.BOOLEAN, "render image only for mobile screen size", "true");
        params.add(DESKTOP_ONLY_KEY, PluginParamType.BOOLEAN, "render image only for desktop screen size", "true");

        params.add(BORDER_KEY, PluginParamType.BOOLEAN, "use border around image", "true");
        params.add(FIT_KEY, PluginParamType.BOOLEAN, "fit image to the text width", "true");
        params.add(SCALE_DEPRECATED_KEY, PluginParamType.NUMBER, "[deprecated] image scale ratio", "0.5");
        params.add(SCALE_KEY, PluginParamType.NUMBER, "image scale ratio", "0.5");
        params.add(PIXEL_RATIO_KEY, PluginParamType.NUMBER,
                "pixel ratio for hi-dpi images, effect is similar to scale, e.g. 2.0 is the same as scale 0.5. " +
                        "The difference is pixelRatio affects annotation coordinates so they need to be " +
                        "supplied using smaller numbers ", "2.0");
        params.add(additionalParameters());

        return params;
    }

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

    protected abstract List<Map<String, Object>> annotationShapes(BufferedImage image);

    protected abstract Double pixelRatio();

    protected abstract PluginParamsDefinition additionalParameters();

    protected abstract Stream<AuxiliaryFile> additionalAuxiliaryFiles();

    protected PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        PluginParamsOpts opts = pluginParams.getOpts();
        pixelRatioFromOpts = opts.has(PIXEL_RATIO_KEY) ?
                opts.getNumber(PIXEL_RATIO_KEY).doubleValue():
                null;

        ResourcesResolver resourceResolver = componentsRegistry.resourceResolver();
        DocStructure docStructure = componentsRegistry.docStructure();
        String imagePath = pluginParams.getFreeParam();

        isExternal = UrlUtils.isExternal(imagePath);
        Map<String, Object> props = new LinkedHashMap<>(opts.toMap());

        if (isExternal) {
            props.put("imageSrc", imagePath);
            props.put("shapes", annotationShapes(null));
            docStructure.validateUrl(markupPath, "<image plugin>", new DocUrl(imagePath));
        } else {
            auxiliaryFile = resourceResolver.runtimeAuxiliaryFile(imagePath);
            BufferedImage bufferedImage = resourceResolver.imageContent(imagePath);

            props.put("imageSrc", docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString()));
            props.put("timestamp", componentsRegistry.timeService().fileModifiedTimeMillis(auxiliaryFile.getPath()));
            props.put("shapes", annotationShapes(bufferedImage));
            setWidthHeight(bufferedImage, props);
        }

        updatePropsScale(props, opts);
        updateTitleProp(props, opts);

        new AnchorFeature(docStructure, markupPath, pluginParams).updateProps(props);

        return PluginResult.docElement("AnnotatedImage", props);
    }

    private void setWidthHeight(BufferedImage bufferedImage,
                                Map<String, Object> props) {
        Double pixelRatio = pixelRatio();

        props.put("width", bufferedImage.getWidth() / pixelRatio);
        props.put("height", bufferedImage.getHeight() / pixelRatio);
    }

    private static void updatePropsScale(Map<String, Object> props, PluginParamsOpts opts) {
        // TODO use deprecation params API
        if (opts.has(SCALE_DEPRECATED_KEY)) {
            props.put(SCALE_KEY, opts.getNumber(SCALE_DEPRECATED_KEY));
        } else if (opts.has(SCALE_KEY)) {
            props.put(SCALE_KEY, opts.getNumber(SCALE_KEY));
        }
    }

    private static void updateTitleProp(Map<String, Object> props, PluginParamsOpts opts) {
        if (opts.has(TITLE_KEY)) {
            props.put(TITLE_KEY, opts.get(TITLE_KEY));
        } else if (opts.has(CAPTION_KEY)) {
            props.put(TITLE_KEY, opts.get(CAPTION_KEY));
        }
    }
}
