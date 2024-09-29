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
import org.testingisdocumenting.znai.structure.AnchorIds;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.utils.NameUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import static org.testingisdocumenting.znai.extensions.PluginParamsDefinitionCommon.TITLE_KEY;

public class AnchorFeature implements PluginFeature {
    public static final PluginParamsDefinition paramsDefinition = createParamsDefinition();

    private static final String ANCHOR_ID_KEY = "anchorId";

    private final DocStructure docStructure;
    private final Path markupPath;
    private final PluginParams pluginParams;

    public AnchorFeature(DocStructure docStructure, Path markupPath, PluginParams pluginParams) {
        this.docStructure = docStructure;
        this.markupPath = markupPath;
        this.pluginParams = pluginParams;
    }

    @Override
    public void updateProps(Map<String, Object> props) {
        String anchorId = pluginParams.getOpts().get(ANCHOR_ID_KEY, "");
        if (!anchorId.isEmpty()) {
            docStructure.registerLocalAnchors(markupPath, new AnchorIds(anchorId, Collections.emptyList()));
            return;
        }

        String title = pluginParams.getOpts().get(TITLE_KEY, props.getOrDefault(TITLE_KEY, "").toString());
        if (!title.isEmpty()) {
            var uniqueAnchorIds = docStructure.generateUniqueAnchors(markupPath, NameUtils.idFromTitle(title));
            docStructure.registerLocalAnchors(markupPath, uniqueAnchorIds);

            props.put(ANCHOR_ID_KEY, uniqueAnchorIds.main());
        }
    }

    private static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add("anchorId", PluginParamType.STRING, "anchor id to use for linking", "\"my_snippet\"");
    }
}
