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
import org.testingisdocumenting.znai.structure.DocStructure;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class PythonCodeEntry {
    private final PythonCodeContext context;

    private final String name;
    private final String type;
    private final String content;
    private final String bodyOnly;
    private final String docString;
    private final List<PythonCodeArg> args;
    private final List<String> decorators;

    public PythonCodeEntry(Map<String, Object> parsed, PythonCodeContext context) {
        this.context = context;

        this.name = Objects.toString(parsed.get("name"), "");
        this.type = Objects.toString(parsed.get("type"), "");
        this.content = Objects.toString(parsed.get("content"), "");
        this.bodyOnly = Objects.toString(parsed.get("body_only"), "");
        this.docString = Objects.toString(parsed.get("doc_string"), "");

        if (this.type.equals("class")) {
            context.registerType(this.name);
        }

        this.args = buildArgs(parsed);
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

    public List<PythonCodeArg> getArgs() {
        return args;
    }

    public List<String> getDecorators() {
        return decorators;
    }

    public Map<String, ?> toMap(DocStructure docStructure) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", name);
        map.put("type", type);
        map.put("args", args.stream().map(arg -> arg.toMap(docStructure)).collect(Collectors.toList()));
        map.put("decorators", decorators);

        return map;
    }

    public ApiParameters createParametersFromPyDoc(DocStructure docStructure, MarkupParser parser, Path parentMarkupPath, String anchorId) {
        ParsedPythonDoc parsedPythonDoc = new ParsedPythonDoc(getDocString());

        ApiParameters apiParameters = new ApiParameters(anchorId);
        parsedPythonDoc.getParams().forEach(pythonParam -> {
            MarkupParserResult parsedMarkdown = parser.parse(parentMarkupPath, pythonParam.getPyDocText());
            apiParameters.add(pythonParam.getName(), paramType(docStructure, pythonParam),
                    parsedMarkdown.contentToListOfMaps(),
                    parsedMarkdown.getAllText());
        });

        return apiParameters;
    }

    private ApiLinkedText paramType(DocStructure docStructure, PythonDocParam param) {
        PythonCodeArg typeHint = getArgs().stream().filter(p -> param.getName().equals(p.getName())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no parameter <" + param.getName() + "> found is signature"));

        return typeHint.getType().isDefined() ?
                typeHint.getType().convertToApiLinkedText(docStructure) :
                new ApiLinkedText(param.getType());
    }

    private List<PythonCodeArg> buildArgs(Map<String, Object> parsed) {
        Object parsedArgs = parsed.get("args");
        if (parsedArgs == null) {
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> parsedArgsList = (List<Map<String, Object>>) parsedArgs;
        return parsedArgsList.stream().map((arg) -> new PythonCodeArg(arg, context)).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<String> extractDecorators(Map<String, Object> parsed) {
        return (List<String>) parsed.get("decorators");
    }
}
