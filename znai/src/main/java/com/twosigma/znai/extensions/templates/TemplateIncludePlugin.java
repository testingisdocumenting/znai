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

package com.twosigma.znai.extensions.templates;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginParamsOpts;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;
import com.twosigma.znai.template.TextTemplate;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TemplateIncludePlugin implements IncludePlugin {
    private Path fullPath;
    private MarkupParserResult parserResult;
    private ResourcesResolver resourcesResolver;
    private Path paramsPath;

    @Override
    public String id() {
        return "template";
    }

    @Override
    public IncludePlugin create() {
        return new TemplateIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();

        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());
        parserResult = parser.parse(markupPath, processTemplate(resourcesResolver.textContent(fullPath),
                pluginParams.getOpts()));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    private String processTemplate(String template, PluginParamsOpts opts) {
        Map<String, Object> params = new HashMap<>(opts.toMap());

        if (opts.has("paramsPath")) {
            paramsPath = resourcesResolver.fullPath(opts.get("paramsPath"));
            params.putAll(JsonUtils.deserializeAsMap(FileUtils.fileTextContent(paramsPath)));
        }

        return new TextTemplate(fullPath.getFileName().toString(), template).process(params);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        Stream<AuxiliaryFile> files = Stream.concat(parserResult.getAuxiliaryFiles().stream(),
                Stream.of(AuxiliaryFile.builtTime(fullPath)));

        return (paramsPath != null) ?
            Stream.concat(Stream.of(AuxiliaryFile.builtTime(paramsPath)), files):
            files;
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(parserResult.getAllText());
    }
}
