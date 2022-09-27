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
import org.testingisdocumenting.znai.extensions.features.PluginFeatureList;

import java.nio.file.Path;

public class SnippetsCommonFeatures {
    private final SnippetAutoTitleFeature snippetAutoTitleFeature;
    private final SnippetRevealLineStopFeature snippetRevealLineStopFeature;
    private final SnippetHighlightFeature snippetHighlightFeature;
    private final CodeReferencesFeature codeReferencesFeature;
    private final AnchorFeature anchorFeature;

    public SnippetsCommonFeatures(ComponentsRegistry componentsRegistry,
                                  Path markupPath,
                                  PluginParams pluginParams,
                                  SnippetContentProvider contentProvider) {
        snippetAutoTitleFeature = new SnippetAutoTitleFeature(contentProvider.snippetId());
        snippetRevealLineStopFeature = new SnippetRevealLineStopFeature(pluginParams, contentProvider);
        snippetHighlightFeature = new SnippetHighlightFeature(componentsRegistry, pluginParams, contentProvider);
        codeReferencesFeature = new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams);
        anchorFeature = new AnchorFeature(componentsRegistry.docStructure(), markupPath, pluginParams);
    }

    public PluginFeatureList asList() {
        return new PluginFeatureList(snippetAutoTitleFeature,
                snippetRevealLineStopFeature,
                snippetHighlightFeature,
                codeReferencesFeature,
                anchorFeature);
    }
}
