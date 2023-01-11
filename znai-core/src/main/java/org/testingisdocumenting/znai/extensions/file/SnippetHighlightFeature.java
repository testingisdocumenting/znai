/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.file;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.features.PluginFeature;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Provides highlight validation, reads highlights from highlightPath when provided
 */
public class SnippetHighlightFeature implements PluginFeature {
    public static final PluginParamsDefinition paramsDefinition = createParamsDefinition();

    private static final String HIGHLIGHT_KEY = "highlight";
    private static final String HIGHLIGHT_PATH_KEY = "highlightPath";

    private final ComponentsRegistry componentsRegistry;
    private final Path highlightFileFullPath;
    private final String highlightPath;
    private final List<Object> highlight;
    private final SnippetContentProvider contentProvider;

    public SnippetHighlightFeature(ComponentsRegistry componentsRegistry, PluginParams pluginParams, SnippetContentProvider contentProvider) {
        this.componentsRegistry = componentsRegistry;

        this.highlightPath = pluginParams.getOpts().get("highlightPath", null);
        this.highlightFileFullPath = highlightPath != null ?
                componentsRegistry.resourceResolver().fullPath(highlightPath) :
                null;

        this.highlight = extractHighlight(pluginParams.getOpts().getList(HIGHLIGHT_KEY));
        this.contentProvider = contentProvider;
    }

    public void updateProps(Map<String, Object> props) {
        if (!highlightProvided()) {
            return;
        }

        SnippetContainerEntriesConverter snippetValidator = new SnippetContainerEntriesConverter(
                contentProvider.snippetId(),
                SnippetCleaner.removeNonAnsiCharacters(contentProvider.snippetContent()),
                HIGHLIGHT_KEY);
        props.put(HIGHLIGHT_KEY, snippetValidator.convertAndValidate(highlight));
    }

    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return highlightFileFullPath != null ?
                Stream.of(AuxiliaryFile.builtTime(highlightFileFullPath)) : Stream.empty();
    }

    private List<Object> extractHighlight(List<Object> highlight) {
        if (highlightFileFullPath == null) {
            return highlight;
        }

        String textContent = componentsRegistry.resourceResolver().textContent(highlightPath);

        List<Object> combined = Arrays.asList(textContent.split("\n"));
        combined.addAll(highlight);

        return combined;
    }

    private boolean highlightProvided() {
        return !highlight.isEmpty();
    }

    private static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add(HIGHLIGHT_KEY, PluginParamType.LIST_OR_SINGLE_STRING_OR_NUMBER,
                        "lines to highlight by index or partial match", "[3, \"constructor\"] or \"class\"")
                .add(HIGHLIGHT_PATH_KEY, PluginParamType.STRING, "path to a file with lines to highlight", "highlight.txt");
    }
}
