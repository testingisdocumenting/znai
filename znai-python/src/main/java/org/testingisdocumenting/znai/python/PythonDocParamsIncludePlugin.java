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

import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.python.pydoc.ParsedPythonDoc;

import java.util.List;
import java.util.Map;

public class PythonDocParamsIncludePlugin extends PythonIncludePluginBase {
    private PythonCodeEntry codeEntry;

    @Override
    public String id() {
        return "python-doc-params";
    }

    @Override
    public IncludePlugin create() {
        return new PythonDocParamsIncludePlugin();
    }

    @Override
    public PythonIncludeResult process(PythonCode parsed) {
        codeEntry = findEntryByName(parsed, entryName);
        ParsedPythonDoc parsedPythonDoc = new ParsedPythonDoc(codeEntry.getDocString());

        ApiParameters apiParameters = new ApiParameters(entryName);
        parsedPythonDoc.getParams().forEach(pythonParam -> {
            MarkupParserResult parsedMarkdown = componentsRegistry.markdownParser().parse(fullPath, pythonParam.getPyDocText());
            apiParameters.add(pythonParam.getName(), paramType(pythonParam),
                    parsedMarkdown.contentToListOfMaps(),
                    parsedMarkdown.getAllText());
        });

        Map<String, Object> props = apiParameters.toMap();
        codeReferencesFeature.updateProps(props);
        props.putAll(pluginParams.getOpts().toMap());

        List<DocElement> docElements = PluginResult.docElement("ApiParameters", props).getDocElements();

        return new PythonIncludeResult(docElements, codeEntry.getDocString());
    }

    private ApiLinkedText paramType(PythonDocParam param) {
        PythonCodeArg typeHint = codeEntry.getArgs().stream().filter(p -> param.getName().equals(p.getName())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no parameter <" + param.getName() + "> found is signature"));
        String hintedType = typeHint.getType().renderTypeAsString();
        if (!hintedType.isEmpty()) {
            return new ApiLinkedText(hintedType);
        }

        return new ApiLinkedText(param.getType());
    }
}
