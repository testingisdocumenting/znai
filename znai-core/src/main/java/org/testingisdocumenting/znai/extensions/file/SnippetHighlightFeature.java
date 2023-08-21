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
import java.util.*;
import java.util.stream.Stream;

/**
 * provides passed highlight validation and reads highlights from highlightPath when provided.
 * converts "highlight regions" to a list of lines to highlight
 */
public class SnippetHighlightFeature implements PluginFeature {
    public static final PluginParamsDefinition paramsDefinition = createParamsDefinition();

    private static final String HIGHLIGHT_KEY = "highlight";
    private static final String HIGHLIGHT_PATH_KEY = "highlightPath";
    private static final String HIGHLIGHT_REGION_KEY = "highlightRegion";
    private static final String HIGHLIGHT_REGION_START_SUB_KEY = "start";
    private static final String HIGHLIGHT_REGION_END_SUB_KEY = "end";
    private static final String HIGHLIGHT_REGION_SCOPE_SUB_KEY = "scope";
    private static final String HIGHLIGHT_REGION_START_FULL_KEY = HIGHLIGHT_REGION_KEY + "." + HIGHLIGHT_REGION_START_SUB_KEY;
    private static final String HIGHLIGHT_REGION_END_FULL_KEY = HIGHLIGHT_REGION_KEY + "." + HIGHLIGHT_REGION_END_SUB_KEY;
    private static final String HIGHLIGHT_REGION_SCOPE_FULL_KEY = HIGHLIGHT_REGION_KEY + "." + HIGHLIGHT_REGION_SCOPE_SUB_KEY;

    private final ComponentsRegistry componentsRegistry;
    private final Path highlightFileFullPath;
    private final String highlightPath;
    private final PluginParams pluginParams;
    private final SnippetContainerEntriesConverter snippetIdxConverter;

    public SnippetHighlightFeature(ComponentsRegistry componentsRegistry, PluginParams pluginParams, SnippetContentProvider contentProvider) {
        this.componentsRegistry = componentsRegistry;
        this.pluginParams = pluginParams;

        this.highlightPath = pluginParams.getOpts().get("highlightPath", null);
        this.highlightFileFullPath = highlightPath != null ?
                componentsRegistry.resourceResolver().fullPath(highlightPath) :
                null;

        snippetIdxConverter = new SnippetContainerEntriesConverter(
                contentProvider.snippetId(),
                SnippetCleaner.removeNonAnsiCharacters(contentProvider.snippetContent()));
    }

    public void updateProps(Map<String, Object> props) {
        Set<Integer> indexes = generateHighlightIndexes(pluginParams);
        if (indexes.isEmpty()) {
            return;
        }

        props.put(HIGHLIGHT_KEY, indexes);
    }

    public Stream<AuxiliaryFile> auxiliaryFiles() {
        return highlightFileFullPath != null ?
                Stream.of(AuxiliaryFile.builtTime(highlightFileFullPath)) : Stream.empty();
    }

    private Set<Integer> generateHighlightIndexes(PluginParams pluginParams) {
        Set<Integer> combined = new TreeSet<>();
        combined.addAll(generateHighlightIndexesFromPassed(pluginParams));
        combined.addAll(generateHighlightIndexesFromFile());
        combined.addAll(generateIndexesFromRegionsList(pluginParams));

        return combined;
    }

    private List<Integer> generateHighlightIndexesFromPassed(PluginParams pluginParams) {
        List<Object> passed = pluginParams.getOpts().getList(HIGHLIGHT_KEY);
        return generateHighlightIndexesFromStringOrIdx(HIGHLIGHT_KEY, passed);
   }

    private List<Integer> generateHighlightIndexesFromFile() {
        if (highlightPath == null) {
            return Collections.emptyList();
        }

        String textContent = componentsRegistry.resourceResolver().textContent(highlightPath);
        List<Object> fromFile = Arrays.asList(textContent.split("\n"));

        return generateHighlightIndexesFromStringOrIdx(HIGHLIGHT_PATH_KEY, fromFile);
    }

    private List<Integer> generateIndexesFromRegionsList(PluginParams pluginParams) {
        List<?> regionList = pluginParams.getOpts().getList(HIGHLIGHT_REGION_KEY);
        return regionList.stream().flatMap(m -> generateIndexesFromRegionMap(m).stream()).toList();
    }

    private List<Integer> generateIndexesFromRegionMap(Object regionRaw) {
        if (!(regionRaw instanceof Map)) {
            throw new IllegalArgumentException(regionWrongFormatMessage());
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> region = (Map<String, Object>) regionRaw;
        if (region.containsKey(HIGHLIGHT_REGION_SCOPE_SUB_KEY)) {
            return generateIndexesFromRegionScope(region);
        } else {
            return generateIndexesFromRegionStartEnd(region);
        }
    }

    private List<Integer> generateIndexesFromRegionScope(Map<String, Object> region) {
        String start = getRequiredStringSubValue(region, HIGHLIGHT_REGION_START_SUB_KEY);
        String scope = getRequiredStringSubValue(region, HIGHLIGHT_REGION_SCOPE_SUB_KEY);
        if (scope.length() != 2) {
            throw new IllegalArgumentException(HIGHLIGHT_REGION_SCOPE_FULL_KEY + " must be in format \"{}\", \"[]\", \"()\" etc");
        }

        int startIdx = snippetIdxConverter.findAndValidateFirstContain(HIGHLIGHT_REGION_START_FULL_KEY, 0, start);
        RegionScopeExtractor regionScopeExtractor = new RegionScopeExtractor(snippetIdxConverter.getLines(), startIdx, scope.charAt(0), scope.charAt(1));
        regionScopeExtractor.process();

        if (regionScopeExtractor.getResultEndLineIdx() == -1) {
            throw new IllegalArgumentException("can't find region to highlight that starts with line: \"" + start + "\" and scoped with: " + scope);
        }

        return generateIndexesFromStartAndEnd(regionScopeExtractor.getResultStartLineIdx(), regionScopeExtractor.getResultEndLineIdx());
    }

    private List<Integer> generateIndexesFromRegionStartEnd(Map<String, Object> region) {
        String start = getRequiredStringSubValue(region, HIGHLIGHT_REGION_START_SUB_KEY);
        String end = getRequiredStringSubValue(region, HIGHLIGHT_REGION_END_SUB_KEY);

        int startIdx = snippetIdxConverter.findAndValidateFirstContain(HIGHLIGHT_REGION_START_FULL_KEY, 0, start);
        int endIdx = snippetIdxConverter.findAndValidateFirstContain(HIGHLIGHT_REGION_END_FULL_KEY, startIdx + 1, end);

        return generateIndexesFromStartAndEnd(startIdx, endIdx);
    }

    private static List<Integer> generateIndexesFromStartAndEnd(int startIdx, int endIdx) {
        List<Integer> result = new ArrayList<>();
        for (int idx = startIdx; idx <= endIdx; idx++) {
            result.add(idx);
        }

        return result;
    }

    private List<Integer> generateHighlightIndexesFromStringOrIdx(String paramKeyForValidation, List<Object> idsOrStrings) {
        return snippetIdxConverter.convertAndValidate(paramKeyForValidation, idsOrStrings);
    }

    private static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add(HIGHLIGHT_KEY, PluginParamType.LIST_OR_SINGLE_STRING_OR_NUMBER,
                        "lines to highlight by index or partial match", "[3, \"constructor\"] or \"class\"")
                .add(HIGHLIGHT_PATH_KEY, PluginParamType.STRING, "path to a file with lines to highlight", "highlight.txt")
                .add(HIGHLIGHT_REGION_KEY, PluginParamType.LIST_OR_OBJECT,
                        "region to highlight", "{start: \"line-a\", end: \"line-b\"}");
    }

    private String getRequiredStringSubValue(Map<String, Object> values, String subKey) {
        Object valueRaw = values.get(subKey);

        if (valueRaw == null) {
            throw new IllegalArgumentException(HIGHLIGHT_REGION_KEY + "." + subKey + " is missing");
        }

        if (!(valueRaw instanceof String)) {
            throw new IllegalArgumentException(regionWrongFormatMessage());
        }

        String value = valueRaw.toString();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(HIGHLIGHT_REGION_KEY + "." + subKey + " can't be empty");
        }

        return value;
    }

    private static String regionWrongFormatMessage() {
        return HIGHLIGHT_REGION_KEY + " should be in format {" + HIGHLIGHT_REGION_START_SUB_KEY + ": \"line-star\", " +
                HIGHLIGHT_REGION_END_SUB_KEY + ": \"line-end\"} or {\"" + HIGHLIGHT_REGION_START_SUB_KEY + "\": \"line-star\", " +
                HIGHLIGHT_REGION_SCOPE_SUB_KEY + ": \"{}\"}";
    }
}
