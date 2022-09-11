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

package org.testingisdocumenting.znai.extensions.card;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CardFencePlugin implements FencePlugin {
    private static final String IMAGE_SRC_KEY = "imageSrc";
    private static final String IMAGE_HEIGHT_KEY = "imageHeight";
    private static final String IMAGE_BACKGROUND_KEY = "imageBackground";

    private AuxiliaryFile imageAuxiliaryFile;
    private MarkupParserResult contentParseResult;
    private boolean isExternal;
    private ComponentsRegistry componentsRegistry;

    @Override
    public String id() {
        return "card";
    }

    @Override
    public FencePlugin create() {
        return new CardFencePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .add(PluginParamsDefinitionCommon.title)
                .add(IMAGE_HEIGHT_KEY, PluginParamType.NUMBER, "force image height", "100")
                .add(IMAGE_BACKGROUND_KEY, PluginParamType.STRING, "image background gradient/color", "#aere83");
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        if (imageAuxiliaryFile != null) {
            return Stream.of(imageAuxiliaryFile);
        }

        return Stream.empty();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.componentsRegistry = componentsRegistry;

        String imageSrc = pluginParams.getFreeParam();
        isExternal = UrlUtils.isExternal(imageSrc);

        imageAuxiliaryFile = createAuxiliaryFileIfRequired(imageSrc);

        contentParseResult = componentsRegistry.markdownParser().parse(markupPath, content);

        Map<String, Object> props = new LinkedHashMap<>(pluginParams.getOpts().toMap());

        if (isExternal) {
            props.put(IMAGE_SRC_KEY, imageSrc);
        } else if (imageAuxiliaryFile != null) {
            props.put(IMAGE_SRC_KEY, componentsRegistry.docStructure().fullUrl(imageAuxiliaryFile.getDeployRelativePath().toString()));
        }

        props.put("bodyContent", contentParseResult.contentToListOfMaps());

        return PluginResult.docElement("Card", props);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(contentParseResult.getAllText());
    }

    private AuxiliaryFile createAuxiliaryFileIfRequired(String imageSrc) {
        if (imageSrc.isEmpty()) {
            return null;
        }

        if (isExternal) {
            return null;
        }

        return componentsRegistry.resourceResolver().runtimeAuxiliaryFile(imageSrc);
    }
}
