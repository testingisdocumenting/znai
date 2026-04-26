/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.extensions.userdefined;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class UserDefinedPluginProcessor {
    private final UserDefinedPluginConfig config;
    private MarkupParserResult parserResult;

    UserDefinedPluginProcessor(UserDefinedPluginConfig config) {
        this.config = config;
    }

    PluginResult process(ComponentsRegistry componentsRegistry,
                         Path markupPath,
                         PluginParams pluginParams,
                         String fenceContent) {
        Map<String, Object> params = buildParams(pluginParams, fenceContent);
        String processedTemplate = config.getCompiledTemplate().process(params);

        parserResult = componentsRegistry.defaultParser().parse(markupPath, processedTemplate);

        return PluginResult.docElements(parserResult.docElement().getContent().stream());
    }

    Stream<AuxiliaryFile> auxiliaryFiles() {
        Stream<AuxiliaryFile> extras = Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(config.getConfigPath()),
                        AuxiliaryFile.builtTime(config.getTemplatePath())),
                config.getAvailableValuesPaths().stream().map(AuxiliaryFile::builtTime));

        Stream<AuxiliaryFile> parsed = parserResult != null ?
                parserResult.auxiliaryFiles().stream() :
                Stream.empty();

        return Stream.concat(parsed, extras);
    }

    List<SearchText> textForSearch() {
        String text = parserResult != null ? parserResult.getAllText() : "";
        return List.of(SearchScore.STANDARD.text(text));
    }

    private Map<String, Object> buildParams(PluginParams pluginParams, String fenceContent) {
        Map<String, Object> params = pluginParams.getOpts().toMap();

        putSpecialArg(params, config.getFreeFormArgument(), pluginParams.getFreeParam(), "free form parameter");
        putSpecialArg(params, config.getFenceContentArgument(), fenceContent, "non-empty fence content");

        return params;
    }

    private void putSpecialArg(Map<String, Object> params, UserDefinedPluginArgument arg, String value, String label) {
        if (arg == null) {
            return;
        }

        if (arg.isRequired() && (value == null || value.isEmpty())) {
            throw new IllegalArgumentException("plugin <" + config.getId() + "> requires " + label);
        }

        params.put(arg.getName(), value == null ? "" : value);
    }
}
