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

import static org.testingisdocumenting.znai.python.PythonIncludeResultBuilder.*;
import static org.testingisdocumenting.znai.python.PythonIncludeResultBuilder.ArgsRenderOpt;
import static org.testingisdocumenting.znai.python.PythonIncludeResultBuilder.NameRenderOpt;

public class PythonMethodIncludePlugin extends PythonIncludePluginBase {
    private PythonUtils.FileNameAndRelativeName fileAndRelativeEntryName;

    @Override
    public String id() {
        return "python-method";
    }

    @Override
    public IncludePlugin create() {
        return new PythonMethodIncludePlugin();
    }

    @Override
    protected String snippetIdToUse() {
        return pluginParams.getFreeParam();
    }

    @Override
    protected Path pathToUse() {
        fileAndRelativeEntryName = PythonUtils.findFileNameAndRelativeNameByFullyQualifiedName(resourcesResolver, pluginParams.getFreeParam());
        return resourcesResolver.fullPath(fileAndRelativeEntryName.getFile());
    }

    @Override
    protected String defaultPackageName() {
        return fileAndRelativeEntryName.getPackageName();
    }

    @Override
    public PythonIncludeResult process(PythonCode parsed, ParserHandler parserHandler, Path markupPath) {
        PythonCodeEntry funcEntry = parsed.findRequiredEntryByTypeAndName("function", fileAndRelativeEntryName.getRelativeName());

        String fullyQualifiedName = pluginParams.getFreeParam();

        PythonIncludeResultBuilder builder = new PythonIncludeResultBuilder(componentsRegistry,
                parserHandler,
                fullyQualifiedName, fileAndRelativeEntryName);

        MarginOpts marginOpts = funcEntry.getDocString().isEmpty() ?
                MarginOpts.DEFAULT: MarginOpts.EXTRA_BOTTOM_MARGIN;

        builder.addMethodSignature(funcEntry, NameRenderOpt.FULL_NAME, ArgsRenderOpt.KEEP_SELF, marginOpts, true);
        builder.addPyDocTextOnly(markupPath, funcEntry);
        builder.addPyDocParams(markupPath, funcEntry);

        return builder.build();
    }
}
