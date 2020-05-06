/*
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
import org.testingisdocumenting.znai.extensions.PluginParams;
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
    private final ComponentsRegistry componentsRegistry;
    private final PluginParams pluginParams;
    private final Path highlightFileFullPath;
    private final String highlightPath;
    private final List<Object> highlight;
    private final SnippetContentProvider contentProvider;

    public SnippetHighlightFeature(ComponentsRegistry componentsRegistry, PluginParams pluginParams, SnippetContentProvider contentProvider) {
        this.componentsRegistry = componentsRegistry;
        this.pluginParams = pluginParams;

        this.highlightPath = pluginParams.getOpts().get("highlightPath", null);
        this.highlightFileFullPath = highlightPath != null ?
                componentsRegistry.resourceResolver().fullPath(highlightPath) :
                null;

        this.highlight = extractHighlight(pluginParams.getOpts().getList("highlight"));
        this.contentProvider = contentProvider;
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

    public void updateProps(Map<String, Object> props) {
        if (!highlightProvided()) {
            return;
        }

        validateHighlight(contentProvider.snippetContent());
        props.put("highlight", highlight);
    }

    private void validateHighlight(String snippetContent) {
        String[] lines = snippetContent.split("\n");
        for (Object idxOrText : highlight) {
            if (idxOrText instanceof Number) {
                validateIdx(lines, (Number) idxOrText);
            } else {
                validateContains(lines, (String) idxOrText);
            }
        }
    }

    private void validateIdx(String[] lines, Number idx) {
        int intValue = idx.intValue();
        if (intValue >= lines.length || intValue < 0) {
            throw new IllegalArgumentException("highlight idx is out of range: " + idx + exceptionIdMessage());
        }
    }

    private void validateContains(String[] lines, String partial) {
        if (Arrays.stream(lines).noneMatch(line -> line.contains(partial))) {
            throw new IllegalArgumentException("highlight text <" + partial + "> is not found" +
                    exceptionIdMessage() +
                    "\n" + contentProvider.snippetContent());
        }
    }

    private String exceptionIdMessage() {
        String id = contentProvider.snippetId();
        if (id.isEmpty()) {
            return "";
        }

        return "\ncheck: " + id;
    }

    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return highlightFileFullPath != null ?
                Stream.of(AuxiliaryFile.builtTime(highlightFileFullPath)) : Stream.empty();
    }

    private boolean highlightProvided() {
        return !highlight.isEmpty();
    }
}
