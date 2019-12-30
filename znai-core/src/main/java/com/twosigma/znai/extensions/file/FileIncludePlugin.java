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

package com.twosigma.znai.extensions.file;

import com.twosigma.znai.codesnippets.CodeSnippetsProps;
import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.parser.docelement.DocElementType;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class FileIncludePlugin implements IncludePlugin {
    private String fileName;
    private String text;

    private CodeReferencesTrait codeReferencesTrait;

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
        codeReferencesTrait = new CodeReferencesTrait(componentsRegistry, pluginParams);
        fileName = pluginParams.getFreeParam();

        text = FilePlugin.extractText(
                componentsRegistry.resourceResolver().textContent(fileName),
                pluginParams.getOpts());

        String providedLang = pluginParams.getOpts().getString("lang");
        String langToUse = (providedLang == null) ? langFromFileName(fileName) : providedLang;

        Map<String, Object> props = CodeSnippetsProps.create(langToUse, text);
        props.putAll(pluginParams.getOpts().toMap());
        codeReferencesTrait.updateProps(props);

        return PluginResult.docElement(DocElementType.SNIPPET, props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(
                        componentsRegistry.resourceResolver().fullPath(fileName))),
                codeReferencesTrait.auxiliaryFiles());
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(text);
    }


    private static String langFromFileName(String fileName) {
        String ext = extFromFileName(fileName);
        switch (ext) {
            case "js": return "javascript";
            default: return ext;
        }
    }

    private static String extFromFileName(String fileName) {
        int dotLastIdx = fileName.lastIndexOf('.');
        if (dotLastIdx == -1) {
            return "";
        }

        return fileName.substring(dotLastIdx + 1);
    }
}
