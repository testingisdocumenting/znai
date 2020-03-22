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

package com.twosigma.znai.cpp.extensions;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.cpp.parser.CodePart;
import com.twosigma.znai.cpp.parser.CppSourceCode;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginParamsOpts;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CppCommentsIncludePlugin implements IncludePlugin {
    private static final String ZNAI_PREFIX = "@znai";

    private Path cppPath;

    @Override
    public String id() {
        return "cpp-comments";
    }

    @Override
    public IncludePlugin create() {
        return new CppCommentsIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String fileName = pluginParams.getFreeParam();
        cppPath = componentsRegistry.resourceResolver().fullPath(fileName);
        String text = componentsRegistry.resourceResolver().textContent(fileName);

        String comments = extractComments(text, pluginParams.getOpts());
        MarkupParserResult parserResult = componentsRegistry.defaultParser().parse(cppPath, comments);
        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(cppPath));
    }

    private String extractComments(String text, PluginParamsOpts opts) {
        String entry = opts.getRequiredString("entry");
        String body = CppSourceCode.entryBodyOnly(text, entry);

        List<CodePart> parts = CppSourceCode.splitOnComments(body);
        return parts.stream().filter(CodePart::isComment)
                .map(cp -> cp.getData().trim())
                .filter(c -> c.startsWith(ZNAI_PREFIX))
                .map(c -> c.replaceAll("^" + ZNAI_PREFIX, "").trim())
                .collect(Collectors.joining("\n\n"));
    }

}
