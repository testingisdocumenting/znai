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

package org.testingisdocumenting.znai.java.extensions;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.features.PluginFeatureList;
import org.testingisdocumenting.znai.extensions.file.CodeReferencesFeature;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.java.parser.JavaCode;
import org.testingisdocumenting.znai.java.parser.html.HtmlToDocElementConverter;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

abstract public class JavaIncludePluginBase implements IncludePlugin {
    protected String path;
    protected Path fullPath;
    protected ComponentsRegistry componentsRegistry;
    protected Path markupPath;
    protected PluginParams pluginParams;
    protected String entry;
    protected List<String> entries;
    private JavaIncludeResult javaIncludeResult;

    protected PluginFeatureList features;
    protected CodeReferencesFeature codeReferencesFeature;

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        codeReferencesFeature = new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams);
        features = new PluginFeatureList(
                codeReferencesFeature
        );

        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;
        this.pluginParams = pluginParams;

        path = pluginParams.getFreeParam();
        fullPath = componentsRegistry.resourceResolver().fullPath(path);
        entry = pluginParams.getOpts().get("entry");
        entries = pluginParams.getOpts().getList("entries");

        if (entry != null && !entries.isEmpty()) {
            throw new IllegalArgumentException("specify either entry or entries");
        }

        JavaCode javaCode = new JavaCode(componentsRegistry.resourceResolver().textContent(path));
        javaIncludeResult = process(javaCode);

        return PluginResult.docElements(javaIncludeResult.getDocElements().stream());
    }

    abstract public JavaIncludeResult process(JavaCode javaCode);

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return features.combineAuxiliaryFilesWith(Stream.of(AuxiliaryFile.builtTime(fullPath)));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(javaIncludeResult.getText());
    }

    protected List<Map<String, Object>> javaDocTextToDocElements(String html) {
        return HtmlToDocElementConverter.convert(
                componentsRegistry, markupPath, html, codeReferencesFeature.getReferences())
                .stream()
                .map(DocElement::toMap)
                .collect(toList());
    }
}
