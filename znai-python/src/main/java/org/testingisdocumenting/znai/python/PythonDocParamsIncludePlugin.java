/*
 * Copyright 2021 znai maintainers
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

import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PythonDocParamsIncludePlugin extends PythonIncludePluginBase {
    @Override
    public String id() {
        return "python-doc-params";
    }

    @Override
    public IncludePlugin create() {
        return new PythonDocParamsIncludePlugin();
    }

    @Override
    public PythonIncludeResult process(PythonCode parsed, ParserHandler parserHandler, Path markupPath) {
        String entryName = getEntryName();

        PythonCodeEntry codeEntry = findEntryByName(parsed, entryName);

        ApiParameters apiParameters = codeEntry.createParametersFromPyDoc(
                componentsRegistry.docStructure(),
                componentsRegistry.markdownParser(),
                fullPath, entryName);

        Map<String, Object> props = apiParameters.toMap();
        codeReferencesFeature.updateProps(props);
        props.putAll(pluginParams.getOpts().toMap());

        List<DocElement> docElements = Collections.singletonList(DocElement.withPropsMap("ApiParameters", props));

        return new PythonIncludeResult(docElements, codeEntry.getDocString());
    }
}
