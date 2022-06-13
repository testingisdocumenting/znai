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
import org.testingisdocumenting.znai.extensions.file.CodeReferencesFeature;
import org.testingisdocumenting.znai.extensions.file.SnippetHighlightFeature;
import org.testingisdocumenting.znai.extensions.validation.EntryPresenceValidation;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public abstract class JsonBasePlugin implements Plugin {
    private final static String INCLUDE_KEY = "include";
    private final static String PATHS_KEY = "paths";
    private final static String PATHS_FILE_KEY = "pathsFile";
    private final static String COLLAPSED_PATHS_KEY = "collapsedPaths";

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
                .add(PluginParamsDefinitionCommon.snippetTitle)
                .add(INCLUDE_KEY, PluginParamType.STRING, "json path to include", "$..book[0,1]")
                .add(PATHS_KEY, PluginParamType.LIST_OR_SINGLE_STRING, "path(s) to leaf values to highlight",
                        "\"root.store.book[0].category\" or " +
                                "[\"root.store.book[0].category\", \"root.store.book[2].category\"]")
                .add(PATHS_FILE_KEY, PluginParamType.STRING,
                        "path to a json file with list of paths to highlight inside", "\"paths.json\"")
                .add(COLLAPSED_PATHS_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "path(s) to nodes to collapse initially",
                        "\"root.store.book\" or [\"root.store.book\", \"root.store.discount\"]")
                .add(SnippetHighlightFeature.paramsDefinition)
                .add(PluginParamsDefinitionCommon.snippetReadMore)
                .add(CodeReferencesFeature.paramsDefinition);

        updateParams(params);

        return params;
    }

    public PluginResult commonProcess(ComponentsRegistry componentsRegistry,
                                      Path markupPath,
                                      PluginParams pluginParams,
                                      String json) {
        resourcesResolver = componentsRegistry.resourceResolver();
        String jsonPath = pluginParams.getOpts().get(INCLUDE_KEY, "$");
        Object content = JsonPath.read(json, jsonPath);

        features = new PluginFeatureList(
                new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams));
        additionalPluginFeatures().forEach(features::add);

        List<String> paths = extractPaths(pluginParams.getOpts());
        validatePaths(content, paths);

        Map<String, Object> props = pluginParams.getOpts().toMap();
        props.put("data", content);
        props.put("paths", paths);
        features.updateProps(props);

        return PluginResult.docElement("Json", props);
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

    private static void validatePaths(Object json, List<String> paths) {
        Set<String> existingPaths = buildPaths(json);
        EntryPresenceValidation.validateItemsPresence("path", "JSON", existingPaths, paths);
    }

    private static Set<String> buildPaths(Object json) {
        return new JsonPaths(json).getPaths();
    }
}
