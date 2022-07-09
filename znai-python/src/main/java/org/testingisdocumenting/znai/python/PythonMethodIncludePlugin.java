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

import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.python.pydoc.ParsedPythonDoc;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PythonMethodIncludePlugin extends PythonIncludePluginBase {
    private PythonUtils.FileNameAndRelativeName fileAndRelativeEntryName;
    private List<DocElement> docElements;
    private List<String> searchText;

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
        List<PythonUtils.FileNameAndRelativeName> fileAndNames = PythonUtils.entityNameFileNameCombos(pluginParams.getFreeParam());

        fileAndRelativeEntryName = fileAndNames.stream()
                .filter(fn -> resourcesResolver.canResolve(fn.getFile()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "can't find any of <" +
                                fileAndNames.stream()
                                        .map(PythonUtils.FileNameAndRelativeName::getFile)
                                        .collect(Collectors.joining(", ")) + ">, tried locations:\n  " +
                                String.join("\n  ") + resourcesResolver.listOfTriedLocations("")));

        return resourcesResolver.fullPath(fileAndRelativeEntryName.getFile());
    }

    @Override
    protected String defaultPackageName() {
        return fileAndRelativeEntryName.getPackageName();
    }

    @Override
    public PythonIncludeResult process(PythonCode parsed, ParserHandler parserHandler, Path markupPath) {
        docElements = new ArrayList<>();
        searchText = new ArrayList<>();

        PythonCodeEntry func = findFunc(parsed);

        addMethodSignature(func);
        addPyDocTextOnly(markupPath, func);
        addPyDocParams(markupPath, func);

        return new PythonIncludeResult(docElements, String.join(" ", searchText));
    }

    private void addMethodSignature(PythonCodeEntry func) {
        Map<String, Object> props = new LinkedHashMap<>(func.toMap(componentsRegistry.docStructure()));
        props.put("qualifiedName", pluginParams.getFreeParam());

        docElements.add(DocElement.withPropsMap("PythonMethod", props));
        searchText.add(fileAndRelativeEntryName.getRelativeName()); // TODO include parameter names into search
    }

    private void addPyDocTextOnly(Path markupPath, PythonCodeEntry func) {
        ParsedPythonDoc parsedPythonDoc = new ParsedPythonDoc(func.getDocString());
        docElements.addAll(componentsRegistry.markdownParser().parse(markupPath, parsedPythonDoc.getPyDocDescriptionOnly())
                .getDocElement().getContent());
        searchText.add(parsedPythonDoc.getPyDocDescriptionOnly());
    }

    private void addPyDocParams(Path markupPath, PythonCodeEntry func) {
        ApiParameters apiParameters = func.createParametersFromPyDoc(componentsRegistry.markdownParser(), markupPath, pluginParams.getFreeParam());
        Map<String, Object> props = apiParameters.toMap();
        props.put("small", true);

        docElements.add(DocElement.withPropsMap("ApiParameters", props));
        searchText.add(apiParameters.combinedTextForSearch());
    }

    private PythonCodeEntry findFunc(PythonCode parsed) {
        PythonCodeEntry func = parsed.findEntryByName(fileAndRelativeEntryName.getRelativeName());
        if (func == null) {
            throw new IllegalArgumentException("can't find entry: " + fileAndRelativeEntryName.getRelativeName());
        }

        if (!func.getType().equals("function")) {
            throw new IllegalArgumentException("found entry by name <" + fileAndRelativeEntryName.getRelativeName() +
                    "> is not a function, but <" + func.getType() + ">");
        }

        return func;
    }
}
