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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class PythonClassIncludePlugin extends PythonIncludePluginBase {
    @Override
    public String id() {
        return "python-class";
    }

    @Override
    public IncludePlugin create() {
        return new PythonClassIncludePlugin();
    }

    @Override
    protected String snippetIdToUse() {
        return pluginParams.getFreeParam();
    }

    @Override
    protected Path pathToUse() {
        return resourcesResolver.fullPath(
                PythonUtils.convertQualifiedNameToFilePath(pluginParams.getFreeParam()));
    }

    @Override
    public PythonIncludeResult process(PythonCode parsed, ParserHandler parserHandler, Path markupPath) {
        String qualifiedName = pluginParams.getFreeParam();

        List<PythonCodeEntry> entries = parsed.findAllEntriesWithPrefix(PythonUtils.entityNameFromQualifiedName(qualifiedName) + ".");
        entries.forEach(entry -> {
            parserHandler.onParagraphStart();
            parserHandler.onSimpleText(entry.getName());
            parserHandler.onParagraphEnd();
        });

        return new PythonIncludeResult(Collections.emptyList(), "");
    }
}
