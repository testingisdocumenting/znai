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

import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.python.pydoc.ParsedPythonDoc;
import org.testingisdocumenting.znai.python.pydoc.PythonDocReturn;
import org.testingisdocumenting.znai.structure.DocStructure;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PythonClass {
    private final String name;
    private final PythonCodeContext context;
    private final List<PythonCodeEntry> members;

    public PythonClass(String name, PythonCodeContext context) {
        this.name = name;
        this.context = context;
        this.members = new ArrayList<>();
        context.registerType(this.name);
    }

    public void addMembers(List<PythonCodeEntry> members) {
        this.members.addAll(members);
    }

    public String getName() {
        return name;
    }

    public List<PythonCodeEntry> getFunctions() {
        return members.stream()
                .filter(entry -> entry.getType().equals("function"))
                .collect(Collectors.toList());
    }

    public ApiParameters createPropertiesAsApiParameters(DocStructure docStructure, MarkupParser parser, Path parentMarkupPath) {
        ApiParameters apiParameters = new ApiParameters(context.getDefaultPackageName() + "_" + name + "_properties");

        List<PythonCodeProperty> properties = generateProperties();
        for (PythonCodeProperty property : properties) {
            ParsedPythonDoc parsedPythonDoc = new ParsedPythonDoc(property.getPyDocText());

            MarkupParserResult parsedMarkdown = parser.parse(parentMarkupPath,
                    (property.isReadOnly() ? "**[readonly]** " : "") +
                            parsedPythonDoc.getPyDocDescriptionOnly());

            apiParameters.add(property.getName(),
                    propertyType(docStructure, property.getType(), parsedPythonDoc.getFuncReturn()),
                    parsedMarkdown.contentToListOfMaps(),
                    parsedMarkdown.getAllText());
        }

        return apiParameters;
    }

    public List<PythonCodeProperty> generateProperties() {
        // raw properties are separate entries with `.get` and `.set` suffixes
        // we convert them to a combined property
        //
        List<PythonCodeEntry> rawProperties = members.stream()
                .filter(member -> member.getType().equals("property"))
                .collect(Collectors.toList());

        Map<String, PythonCodeEntry> entriesGet = new HashMap<>();
        Map<String, PythonCodeEntry> entriesSet = new HashMap<>();

        for (PythonCodeEntry rawProperty : rawProperties) {
            PythonUtils.PropertyNameAndQualifier propertyNameAndQualifier =
                    PythonUtils.extractPropertyNameAndQualifierFromEntryName(rawProperty.getName());

            String name = propertyNameAndQualifier.getName();
            String qualifier = propertyNameAndQualifier.getQualifier();

            if (qualifier.equals("get")) {
                entriesGet.put(name, rawProperty);
            } else if (qualifier.equals("set")) {
                entriesSet.put(name, rawProperty);
            } else {
                throw new IllegalArgumentException("unrecognized qualifier <" + qualifier + ">: " + rawProperty.getName());
            }
        }

        return entriesGet.keySet().stream()
                .map(name -> {
                    PythonCodeEntry entryGet = entriesGet.get(name);
                    return new PythonCodeProperty(name, entryGet.getReturns(), !entriesSet.containsKey(name), entryGet.getDocString());
                })
                .collect(Collectors.toList());
    }

    private ApiLinkedText propertyType(DocStructure docStructure, PythonCodeType hintType, PythonDocReturn docReturn) {
        return hintType.isDefined() ?
                hintType.convertToApiLinkedText(docStructure) :
                new ApiLinkedText(docReturn.getType());
    }

}
