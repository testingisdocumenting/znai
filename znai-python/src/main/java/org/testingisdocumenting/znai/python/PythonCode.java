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
import java.util.stream.Stream;

public class PythonCode {
    private final Map<String, PythonCodeEntry> entryByName;
    private final List<Map<String, Object>> parsed;
    private final PythonCodeContext context;

    public PythonCode(List<Map<String, Object>> parsed, PythonCodeContext context) {
        this.parsed = parsed;
        this.context = context;
        entryByName = new LinkedHashMap<>();
        parsed.forEach(p -> {
            PythonCodeEntry entry = new PythonCodeEntry(p, context);
            entryByName.put(entry.getName(), entry);
        });
    }

    public List<Map<String, Object>> getParsed() {
        return parsed;
    }

    public Stream<String> namesStream() {
        return entryByName.keySet().stream();
    }

    public PythonCodeEntry findEntryByName(String name) {
        return entryByName.get(name);
    }

    public PythonCodeEntry findRequiredEntryByTypeAndName(String type, String name) {
        PythonCodeEntry entry = findEntryByName(name);
        if (entry == null) {
            throw new IllegalArgumentException("can't find entry: " + name);
        }

        if (!entry.getType().equals(type)) {
            throw new IllegalArgumentException("found entry by name <" + name + "> is not a " + type + ", but <" + entry.getType() + ">");
        }

        return entry;
    }

    public List<PythonCodeEntry> findAllEntriesWithPrefix(String prefix) {
        return entryByName.values().stream()
                .filter(e -> e.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<PythonCodeEntry> findAllEntriesByTypeWithPrefix(String type, String prefix) {
        return entryByName.values().stream()
                .filter(e -> e.getType().equals(type) && e.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public ApiParameters createPropertiesAsApiParameters(DocStructure docStructure, MarkupParser parser, Path parentMarkupPath, String className) {
        ApiParameters apiParameters = new ApiParameters(context.getDefaultPackageName() + "_" + className + "_properties");

        List<PythonCodeProperty> properties = generatePropertiesForPrefix(className + ".");
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

    public List<PythonCodeProperty> generatePropertiesForPrefix(String prefix) {
        // raw properties are separate entries with `.get` and `.set` suffixes
        // we convert them to a combined property
        //
        List<PythonCodeEntry> rawProperties = findAllEntriesByTypeWithPrefix("property", prefix);

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
