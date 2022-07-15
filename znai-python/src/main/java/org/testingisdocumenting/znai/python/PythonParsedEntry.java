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

import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.python.pydoc.ParsedPythonDoc;
import org.testingisdocumenting.znai.python.pydoc.PythonDocReturn;
import org.testingisdocumenting.znai.structure.DocStructure;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class PythonParsedEntry {
    private final PythonContext context;

    private final String name;
    private final String type;
    private final String content;
    private final String bodyOnly;
    private final String docString;
    private final List<PythonArg> args;
    private final PythonType returns;

    private final List<String> decorators;

    public PythonParsedEntry(Map<String, Object> parsed, PythonContext context) {
        this.context = context;

        this.name = Objects.toString(parsed.get("name"), "");
        this.type = Objects.toString(parsed.get("type"), "");
        this.content = Objects.toString(parsed.get("content"), "");
        this.bodyOnly = Objects.toString(parsed.get("body_only"), "");
        this.docString = Objects.toString(parsed.get("doc_string"), "");

        this.args = buildArgs(parsed);
        this.returns = buildReturns(parsed);
        this.decorators = extractDecorators(parsed);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }

    public String getDocString() {
        return docString;
    }

    public List<PythonArg> getArgs() {
        return args;
    }

    public PythonType getReturns() {
        return returns;
    }

    public List<String> getDecorators() {
        return decorators;
    }

    public boolean isStatic() {
        return decorators.contains("staticmethod");
    }

    public boolean isClassMethod() {
        return decorators.contains("classmethod");
    }

    public boolean isPrivate() {
        String shortName = PythonUtils.entityNameFromQualifiedName(name);
        return shortName.startsWith("_") && !shortName.equals("__init__");
    }

    public Map<String, ?> toMap(DocStructure docStructure) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("type", type);
        map.put("args", args.stream().map(arg -> arg.toMap(docStructure)).collect(Collectors.toList()));
        map.put("returns", returns.convertToApiLinkedText(docStructure).toListOfMaps());
        map.put("decorators", decorators);

        return map;
    }

    public ApiParameters createParametersFromPyDoc(DocStructure docStructure, MarkupParser parser, Path parentMarkupPath, String anchorId) {
        ParsedPythonDoc parsedPythonDoc = new ParsedPythonDoc(getDocString());

        ApiParameters apiParameters = new ApiParameters(anchorId);

        PythonDocReturn funcReturn = parsedPythonDoc.getFuncReturn();
        if (funcReturn.isDefined()) {
            MarkupParserResult parsedMarkdown = parser.parse(parentMarkupPath, funcReturn.getPyDocText());
            apiParameters.add("returns", returnsType(docStructure, funcReturn),
                    parsedMarkdown.contentToListOfMaps(),
                    parsedMarkdown.getAllText());
        }

        parsedPythonDoc.getParams().forEach(pythonParam -> {
            MarkupParserResult parsedMarkdown = parser.parse(parentMarkupPath, pythonParam.getPyDocText());
            apiParameters.add(pythonParam.getName(), paramType(docStructure, pythonParam),
                    parsedMarkdown.contentToListOfMaps(),
                    parsedMarkdown.getAllText());
        });

        return apiParameters;
    }

    private ApiLinkedText returnsType(DocStructure docStructure, PythonDocReturn funcReturn) {
        PythonType returnTypeHint = getReturns();

        return returnTypeHint.isDefined() ?
                returnTypeHint.convertToApiLinkedText(docStructure) :
                new ApiLinkedText(funcReturn.getType());
    }

    private ApiLinkedText paramType(DocStructure docStructure, PythonDocParam param) {
        PythonArg typeHint = getArgs().stream().filter(p -> param.getName().equals(p.getName())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no parameter <" + param.getName() + "> found is signature"));

        return typeHint.getType().isDefined() ?
                typeHint.getType().convertToApiLinkedText(docStructure) :
                new ApiLinkedText(param.getType());
    }

    private List<PythonArg> buildArgs(Map<String, Object> parsed) {
        Object parsedArgs = parsed.get("args");
        if (parsedArgs == null) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> parsedArgsList = (List<Map<String, Object>>) parsedArgs;
        return parsedArgsList.stream().map((arg) -> new PythonArg(arg, context)).collect(Collectors.toList());
    }

    private PythonType buildReturns(Map<String, Object> parsed) {
        return new PythonType(parsed.get("returns"), context);
    }

    @SuppressWarnings("unchecked")
    private List<String> extractDecorators(Map<String, Object> parsed) {
        Object decoratorsValue = parsed.get("decorators");
        return decoratorsValue == null ? Collections.emptyList() : (List<String>) decoratorsValue;
    }
}
