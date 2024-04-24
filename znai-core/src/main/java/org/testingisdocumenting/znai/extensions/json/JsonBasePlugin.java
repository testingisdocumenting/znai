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

package org.testingisdocumenting.znai.extensions.json;

import com.jayway.jsonpath.JsonPath;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.extensions.features.PluginFeature;
import org.testingisdocumenting.znai.extensions.features.PluginFeatureList;
import org.testingisdocumenting.znai.extensions.file.AnchorFeature;
import org.testingisdocumenting.znai.extensions.file.CodeReferencesFeature;
import org.testingisdocumenting.znai.extensions.file.SnippetHighlightFeature;
import org.testingisdocumenting.znai.extensions.validation.EntryPresenceValidation;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.parser.table.CsvTableParser;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public abstract class JsonBasePlugin implements Plugin {
    private final static String INCLUDE_KEY = "include";
    private final static String HIGHLIGHT_KEYS_KEY = "highlightKey";
    private final static String HIGHLIGHT_KEYS_FILE_KEY = "highlightKeyFile";
    private final static String HIGHLIGHT_VALUES_KEY = "highlightValue";
    private final static String HIGHLIGHT_VALUES_FILE_KEY = "highlightValueFile";
    private final static String COLLAPSED_PATHS_KEY = "collapsedPaths";
    private final static String ENCLOSE_IN_OBJECT_KEY = "encloseInObject";
    private final static String CALLOUTS_KEY = "callouts";
    private final static String CALLOUTS_FILE_KEY = "calloutsFile";
    private final static String HIGHLIGHT_VALUES_DEPRECATED_KEY = "paths";
    private final static String HIGHLIGHT_VALUES_DEPRECATED_FILE_KEY = "pathsFile";

    private Path highlightValuesFilePath;
    private Path highlightKeysFilePath;
    private Path calloutsFilePath;

    protected PluginFeatureList features;
    protected ResourcesResolver resourcesResolver;
    private MarkdownParser markdownParser;

    @Override
    public String id() {
        return "json";
    }

    @Override
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition params = new PluginParamsDefinition()
                .add(PluginParamsDefinitionCommon.title)
                .add(INCLUDE_KEY, PluginParamType.STRING, "json path to include", "$..book[0,1]")
                .add(HIGHLIGHT_VALUES_KEY, PluginParamType.LIST_OR_SINGLE_STRING, "path(s) to leaf values to highlight",
                        "\"root.store.book[0].category\" or " +
                                "[\"root.store.book[0].category\", \"root.store.book[2].category\"]")
                .add(HIGHLIGHT_VALUES_FILE_KEY, PluginParamType.STRING,
                        "path to a json file with list of paths to highlight values", "\"paths.json\"")
                .add(HIGHLIGHT_KEYS_KEY, PluginParamType.LIST_OR_SINGLE_STRING, "path(s) to key to highlight",
                        "\"root.store.book[0].category\" or " +
                                "[\"root.store.book[0].category\", \"root.store.book[2].category\"]")
                .add(HIGHLIGHT_KEYS_FILE_KEY, PluginParamType.STRING,
                        "path to a json file with list of paths to highlight keys", "\"paths.json\"")
                .add(COLLAPSED_PATHS_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "path(s) to nodes to collapse initially",
                        "\"root.store.book\" or [\"root.store.book\", \"root.store.discount\"]")
                .add(ENCLOSE_IN_OBJECT_KEY, PluginParamType.STRING,
                        "wrap resulting json into an extra object with provided parent(s)",
                        "\"person.address\"")
                .add(CALLOUTS_KEY, PluginParamType.OBJECT,
                        "callout text per json path",
                        "{\"root.key1.nested[0]\": \"important value for config\"")
                .add(CALLOUTS_FILE_KEY, PluginParamType.STRING,
                        "json or csv file with callout text per json path",
                        "\"common-callouts.json\"")
                .add(SnippetHighlightFeature.paramsDefinition)
                .add(PluginParamsDefinitionCommon.snippetReadMore)
                .add(CodeReferencesFeature.paramsDefinition)
                .add(AnchorFeature.paramsDefinition)
                .rename(HIGHLIGHT_VALUES_DEPRECATED_KEY, HIGHLIGHT_VALUES_KEY)
                .rename(HIGHLIGHT_VALUES_DEPRECATED_FILE_KEY, HIGHLIGHT_VALUES_FILE_KEY);

        registerAdditionalParams(params);

        return params;
    }

    public PluginResult commonProcess(ComponentsRegistry componentsRegistry,
                                      Path markupPath,
                                      PluginParams pluginParams,
                                      String jsonText) {
        resourcesResolver = componentsRegistry.resourceResolver();
        markdownParser = componentsRegistry.markdownParser();
        PluginParamsOpts opts = pluginParams.getOpts();

        String jsonPath = opts.get(INCLUDE_KEY, "$");
        Object parsed = JsonPath.read(jsonText, jsonPath);
        parsed = encloseInObjectIfRequired(opts, parsed);

        features = new PluginFeatureList(new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams));
        additionalPluginFeatures().forEach(features::add);
        features.add(new AnchorFeature(componentsRegistry.docStructure(), markupPath, pluginParams));

        Set<String> existingPaths = buildPaths(parsed);

        List<String> highlightValues = extractHighlightValues(opts);
        EntryPresenceValidation.validateItemsPresence(HIGHLIGHT_VALUES_KEY, "JSON", existingPaths, highlightValues);

        List<String> highlightKeys = extractHighlightKeys(opts);
        EntryPresenceValidation.validateItemsPresence(HIGHLIGHT_KEYS_KEY, "JSON", existingPaths, highlightKeys);

        List<String> collapsedPaths = opts.getList(COLLAPSED_PATHS_KEY);
        EntryPresenceValidation.validateItemsPresence(COLLAPSED_PATHS_KEY, "JSON", existingPaths, collapsedPaths);

        Map<String, List<Map<String, Object>>> docElementDescByPath = extractAndParseCallouts(markupPath, opts);
        EntryPresenceValidation.validateItemsPresence(CALLOUTS_KEY, "JSON", existingPaths, docElementDescByPath.keySet());

        Map<String, Object> props = opts.toMap();
        props.put("data", parsed);
        props.put("highlightValues", highlightValues);
        props.put("highlightKeys", highlightKeys);

        if (!docElementDescByPath.isEmpty()) {
            props.put("calloutsByPath", docElementDescByPath);
        }

        features.updateProps(props);

        props.remove(HIGHLIGHT_KEYS_KEY);
        props.remove(HIGHLIGHT_KEYS_FILE_KEY);
        props.remove(HIGHLIGHT_VALUES_KEY);
        props.remove(HIGHLIGHT_VALUES_FILE_KEY);
        props.remove(HIGHLIGHT_VALUES_DEPRECATED_KEY);
        props.remove(HIGHLIGHT_VALUES_DEPRECATED_FILE_KEY);
        props.remove(CALLOUTS_KEY);
        
        return PluginResult.docElement("Json", props);
    }

    private Object encloseInObjectIfRequired(PluginParamsOpts opts, Object original) {
        String enclosePath = opts.getString(ENCLOSE_IN_OBJECT_KEY);
        if (enclosePath == null) {
            return original;
        }

        enclosePath = enclosePath.trim();

        if (enclosePath.isEmpty()) {
            throw new IllegalArgumentException(ENCLOSE_IN_OBJECT_KEY + " can't be empty");
        }

        Map<String, Object> result = new HashMap<>();

        String[] parts = enclosePath.split("\\.");
        Map<String, Object> cursor = result;
        for (int idx = 0; idx < parts.length - 1; idx++) {
            String part = parts[idx];

            Map<String, Object> entry = new HashMap<>();
            cursor.put(part, entry);

            cursor = entry;
        }

        cursor.put(parts[parts.length - 1], original);

        return result;
    }

    abstract protected void registerAdditionalParams(PluginParamsDefinition paramsDefinition);
    abstract protected Stream<PluginFeature> additionalPluginFeatures();
    abstract protected Stream<AuxiliaryFile> additionalAuxiliaryFiles();

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        Stream<AuxiliaryFile> pathsFile = highlightValuesFilePath == null ?
                Stream.empty() :
                Stream.of(AuxiliaryFile.builtTime(highlightValuesFilePath));

        Stream<AuxiliaryFile> highlightKeysFile = highlightKeysFilePath == null ?
                Stream.empty() :
                Stream.of(AuxiliaryFile.builtTime(highlightKeysFilePath));

        Stream<AuxiliaryFile> calloutsFile = calloutsFilePath == null ?
                Stream.empty() :
                Stream.of(AuxiliaryFile.builtTime(calloutsFilePath));

        return Stream.concat(pathsFile,
                Stream.concat(highlightKeysFile,
                        Stream.concat(calloutsFile,
                                Stream.concat(features.auxiliaryFiles(), additionalAuxiliaryFiles()))));
    }

    @SuppressWarnings("unchecked")
    private List<String> extractHighlightValues(PluginParamsOpts opts) {
        if (opts.has(HIGHLIGHT_VALUES_FILE_KEY)) {
            String filePath = opts.get(HIGHLIGHT_VALUES_FILE_KEY);
            highlightValuesFilePath = resourcesResolver.fullPath(filePath);

            return (List<String>) JsonUtils.deserializeAsList(resourcesResolver.textContent(filePath));
        }

        return opts.getList(HIGHLIGHT_VALUES_KEY);
    }

    @SuppressWarnings("unchecked")
    private List<String> extractHighlightKeys(PluginParamsOpts opts) {
        if (opts.has(HIGHLIGHT_KEYS_FILE_KEY)) {
            String filePath = opts.get(HIGHLIGHT_KEYS_FILE_KEY);
            highlightKeysFilePath = resourcesResolver.fullPath(filePath);

            return (List<String>) JsonUtils.deserializeAsList(resourcesResolver.textContent(filePath));
        }

        return opts.getList(HIGHLIGHT_KEYS_KEY);
    }

    private Map<String, List<Map<String, Object>>> extractAndParseCallouts(Path parentFilePath, PluginParamsOpts opts) {
        if (opts.has(CALLOUTS_KEY)) {
            Map<String, String> markdownByPath = opts.get(CALLOUTS_KEY);
            return convertMarkdownToDocElements(markdownParser, parentFilePath, markdownByPath);
        } else if (opts.has(CALLOUTS_FILE_KEY)) {
            String filePath = opts.get(CALLOUTS_FILE_KEY);
            calloutsFilePath = resourcesResolver.fullPath(filePath);

            Map<String, ?> markdownByPath = parseCallouts(resourcesResolver.textContent(filePath));
            return convertMarkdownToDocElements(markdownParser, parentFilePath, markdownByPath);
        } else {
            return Collections.emptyMap();
        }
    }

    private Map<String, ?> parseCallouts(String content) {
        content = content.trim();
        return content.startsWith("{") ?
                parseCalloutsJson(content) :
                parseCalloutsCsv(content);
    }

    private Map<String, ?> parseCalloutsCsv(String content) {
        MarkupTableData tableData = CsvTableParser.parseWithHeader(content, "path", "markdown");

        Map<String, String> result = new LinkedHashMap<>();
        tableData.forEachRow(row -> result.put(row.get(0), row.get(1)));

        return result;
    }

    private Map<String, ?> parseCalloutsJson(String content) {
        return JsonUtils.deserializeAsMap(content);
    }

    private Map<String, List<Map<String, Object>>> convertMarkdownToDocElements(MarkdownParser parser, Path parentFilePath,
                                                                                Map<String, ?> markdownByPath) {
        Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();

        markdownByPath.forEach((nodePath, markdown) -> {
            MarkupParserResult parseResult = parser.parse(parentFilePath, markdown.toString());
            List<Map<String, Object>> docElements = parseResult.docElement().contentToListOfMaps();

            result.put(nodePath, docElements);
        });

        return result;
    }

    private static Set<String> buildPaths(Object json) {
        return new JsonPaths(json).getPaths();
    }
}
