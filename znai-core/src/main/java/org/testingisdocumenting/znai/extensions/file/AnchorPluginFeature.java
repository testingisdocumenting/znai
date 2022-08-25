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

package org.testingisdocumenting.znai.extensions.file;

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.features.PluginFeature;
import org.testingisdocumenting.znai.structure.DocStructure;

import java.nio.file.Path;
import java.util.Map;

public class AnchorPluginFeature implements PluginFeature {
    public static final PluginParamsDefinition paramsDefinition = createParamsDefinition();

    private static final String ANCHOR_ID_KEY = "anchorId";

    private final DocStructure docStructure;
    private final Path markupPath;
    private final PluginParams pluginParams;

    public AnchorPluginFeature(DocStructure docStructure, Path markupPath, PluginParams pluginParams) {
        this.docStructure = docStructure;
        this.markupPath = markupPath;
        this.pluginParams = pluginParams;
    }

    @Override
    public void updateProps(Map<String, Object> props) {
        String anchorId = pluginParams.getOpts().get(ANCHOR_ID_KEY, "");
        if (anchorId.isEmpty()) {
            return;
        }

        docStructure.registerLocalAnchor(markupPath, anchorId);
    }

    private static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add("anchorId", PluginParamType.STRING, "anchor id to use for linking", "\"my_snippet\"");
    }
}
