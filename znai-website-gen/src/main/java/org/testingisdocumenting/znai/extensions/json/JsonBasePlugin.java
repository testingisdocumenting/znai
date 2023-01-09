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
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public abstract class JsonBasePlugin implements Plugin {
    private final static String INCLUDE_KEY = "include";
    private final static String PATHS_KEY = "paths";
    private final static String PATHS_FILE_KEY = "pathsFile";
    private final static String COLLAPSED_PATHS_KEY = "collapsedPaths";
    private final static String ENCLOSE_IN_OBJECT_KEY = "encloseInObject";

    private Path pathsFilePath;

    protected PluginFeatureList features;
    protected ResourcesResolver resourcesResolver;

    @Override
    public String id() {
        return "json";
    }

    @Override
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition params = new PluginParamsDefinition()
                .add(PluginParamsDefinitionCommon.title)
                .add(INCLUDE_KEY, PluginParamType.STRING, "json path to include", "$..book[0,1]")
                .add(PATHS_KEY, PluginParamType.LIST_OR_SINGLE_STRING, "path(s) to leaf values to highlight",
                        "\"root.store.book[0].category\" or " +
                                "[\"root.store.book[0].category\", \"root.store.book[2].category\"]")
                .add(PATHS_FILE_KEY, PluginParamType.STRING,
                        "path to a json file with list of paths to highlight inside", "\"paths.json\"")
                .add(COLLAPSED_PATHS_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "path(s) to nodes to collapse initially",
                        "\"root.store.book\" or [\"root.store.book\", \"root.store.discount\"]")
                .add(ENCLOSE_IN_OBJECT_KEY, PluginParamType.STRING,
                        "wrap resulting json into an extra object with provided parent(s)",
                        "\"person.address\"")
                .add(SnippetHighlightFeature.paramsDefinition)
                .add(PluginParamsDefinitionCommon.snippetReadMore)
                .add(CodeReferencesFeature.paramsDefinition)
                .add(AnchorFeature.paramsDefinition);

        updateParams(params);

        return params;
    }

    public PluginResult commonProcess(ComponentsRegistry componentsRegistry,
                                      Path markupPath,
                                      PluginParams pluginParams,
                                      String jsonText) {
        resourcesResolver = componentsRegistry.resourceResolver();
        PluginParamsOpts opts = pluginParams.getOpts();

        String jsonPath = opts.get(INCLUDE_KEY, "$");
        Object parsed = JsonPath.read(jsonText, jsonPath);
        parsed = encloseInObjectIfRequired(opts, parsed);

        features = new PluginFeatureList(new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams));
        additionalPluginFeatures().forEach(features::add);
        features.add(new AnchorFeature(componentsRegistry.docStructure(), markupPath, pluginParams));

        Set<String> existingPaths = buildPaths(parsed);

        List<String> paths = extractPaths(opts);
        EntryPresenceValidation.validateItemsPresence(PATHS_KEY, "JSON", existingPaths, paths);

        List<String> collapsedPaths = opts.getList(COLLAPSED_PATHS_KEY);
        EntryPresenceValidation.validateItemsPresence(COLLAPSED_PATHS_KEY, "JSON", existingPaths, collapsedPaths);

        Map<String, Object> props = opts.toMap();
        props.put("data", parsed);
        props.put("paths", paths);
        features.updateProps(props);

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

    abstract protected void updateParams(PluginParamsDefinition paramsDefinition);
    abstract protected Stream<PluginFeature> additionalPluginFeatures();
    abstract protected Stream<AuxiliaryFile> additionalAuxiliaryFiles();

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        Stream<AuxiliaryFile> pathsFile = pathsFilePath == null ?
                Stream.empty() :
                Stream.of(AuxiliaryFile.builtTime(pathsFilePath));

        return Stream.concat(pathsFile, Stream.concat(features.auxiliaryFiles(), additionalAuxiliaryFiles()));
    }

    @SuppressWarnings("unchecked")
    private List<String> extractPaths(PluginParamsOpts opts) {
        if (opts.has("pathsFile")) {
            String filePath = opts.get("pathsFile");
            pathsFilePath = resourcesResolver.fullPath(filePath);

            return (List<String>) JsonUtils.deserializeAsList(resourcesResolver.textContent(filePath));
        }

        return opts.getList(PATHS_KEY);
    }

    private static Set<String> buildPaths(Object json) {
        return new JsonPaths(json).getPaths();
    }
}
