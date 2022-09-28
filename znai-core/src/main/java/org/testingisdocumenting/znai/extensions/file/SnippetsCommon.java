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

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinitionCommon;

import java.nio.file.Path;

public class SnippetsCommon {
    private SnippetsCommon() {
    }

    public static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add(PluginParamsDefinitionCommon.snippetRender)
                .add(SnippetAutoTitleFeature.paramsDefinition)
                .add(SnippetHighlightFeature.paramsDefinition)
                .add(ManipulatedSnippetContentProvider.paramsDefinition)
                .add(SnippetRevealLineStopFeature.paramsDefinition)
                .add(CodeReferencesFeature.paramsDefinition);
    }

    public static SnippetsCommonFeatures createCommonFeatures(ComponentsRegistry componentsRegistry,
                                                              Path markupPath,
                                                              PluginParams pluginParams,
                                                              SnippetContentProvider contentProvider) {
        return new SnippetsCommonFeatures(componentsRegistry, markupPath, pluginParams, contentProvider);
    }
}
