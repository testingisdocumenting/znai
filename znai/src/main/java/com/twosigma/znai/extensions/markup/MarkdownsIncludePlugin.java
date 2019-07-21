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

package com.twosigma.znai.extensions.markup;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.parser.docelement.DocElement;
import com.twosigma.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarkdownsIncludePlugin implements IncludePlugin {
    private List<Path> markdowns;

    @Override
    public String id() {
        return "markdowns";
    }

    @Override
    public IncludePlugin create() {
        return new MarkdownsIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        Path dir = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        MarkupParser parser = componentsRegistry.defaultParser();

        markdowns = markdowns(dir).collect(Collectors.toList());
        Stream<DocElement> elements = markdowns.stream().flatMap(p -> {
            MarkupParserResult parserResult = parser.parse(markupPath, FileUtils.fileTextContent(p));
            return parserResult.getDocElement().getContent().stream();
        });

        return PluginResult.docElements(elements);
    }

    private Stream<Path> markdowns(Path dir) {
        try {
            return Files.list(dir).filter(f -> f.toString().endsWith(".md")).sorted(Comparator.comparing(Path::toString).reversed());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return markdowns.stream().map(AuxiliaryFile::builtTime);
    }
}
