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

import org.testingisdocumenting.znai.codesnippets.CodeSnippetsProps;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.extensions.features.PluginFeatureList;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class FileIncludePlugin implements IncludePlugin {
    private String fileName;

    private PluginFeatureList features;

    private ManipulatedSnippetContentProvider contentProvider;
    private String lang;

    @Override
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition result = new PluginParamsDefinition();

        result.add("lang", PluginParamType.STRING,
                "force language to use for syntax highlight (by default is taken from file extension)", "yaml");

        result.add(SnippetsCommon.createParamsDefinition());

        return result;
    }

    @Override
    public String id() {
        return "file";
    }

    @Override
    public IncludePlugin create() {
        return new FileIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();

        contentProvider = new ManipulatedSnippetContentProvider(fileName,
                componentsRegistry.resourceResolver().textContent(fileName),
                pluginParams);

        features = new PluginFeatureList();
        features.add(SnippetsCommon.createCommonFeatures(componentsRegistry, markupPath, pluginParams, contentProvider).asList());

        this.lang = getLang(pluginParams);
        Map<String, Object> props = CodeSnippetsProps.create(lang, contentProvider.snippetContent());
        props.putAll(pluginParams.getOpts().toMap());

        features.updateProps(props);

        return PluginResult.docElement(DocElementType.SNIPPET, props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return features.combineAuxiliaryFilesWith(
                Stream.of(AuxiliaryFile.builtTime(componentsRegistry.resourceResolver().fullPath(fileName))));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(contentProvider.snippetContent());
    }

    @Override
    public String markdownRepresentation() {
        if (contentProvider == null) {
            return "";
        }
        
        StringBuilder markdown = new StringBuilder();
        markdown.append("```").append(lang).append("\n");
        markdown.append(contentProvider.snippetContent());
        if (!contentProvider.snippetContent().endsWith("\n")) {
            markdown.append("\n");
        }
        markdown.append("```");
        
        return markdown.toString();
    }

    private String getLang(PluginParams pluginParams) {
        var providedLang = pluginParams.getOpts().getString("lang");
        return (providedLang == null) ? langFromFileName(fileName) : providedLang;
    }

    private static String langFromFileName(String fileName) {
        return extFromFileName(fileName);
    }

    private static String extFromFileName(String fileName) {
        int dotLastIdx = fileName.lastIndexOf('.');
        if (dotLastIdx == -1) {
            return "";
        }

        return fileName.substring(dotLastIdx + 1);
    }
}
