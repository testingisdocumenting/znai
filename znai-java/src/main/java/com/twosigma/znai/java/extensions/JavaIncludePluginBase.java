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

package com.twosigma.znai.java.extensions;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.java.parser.JavaCode;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

abstract public class JavaIncludePluginBase implements IncludePlugin {
    protected Path fullPath;
    protected ComponentsRegistry componentsRegistry;
    protected Path markupPath;
    protected PluginParams pluginParams;
    protected String entry;
    protected List<String> entries;
    private JavaIncludeResult javaIncludeResult;

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;
        this.pluginParams = pluginParams;
        fullPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        entry = pluginParams.getOpts().get("entry");
        entries = pluginParams.getOpts().get("entries");

        if (entry != null && entries != null) {
            throw new IllegalArgumentException("specify either entry or entries");
        }

        JavaCode javaCode = new JavaCode(componentsRegistry.resourceResolver().textContent(pluginParams.getFreeParam()));
        javaIncludeResult = process(javaCode);

        return PluginResult.docElements(javaIncludeResult.getDocElements().stream());
    }

    abstract public JavaIncludeResult process(JavaCode javaCode);

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.HIGH.text(javaIncludeResult.getText());
    }
}
