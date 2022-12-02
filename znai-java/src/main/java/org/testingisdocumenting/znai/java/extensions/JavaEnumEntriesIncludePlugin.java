/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.java.extensions;

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.file.CodeReferencesFeature;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.java.parser.EnumEntry;
import org.testingisdocumenting.znai.java.parser.JavaCode;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class JavaEnumEntriesIncludePlugin extends JavaIncludePluginBase {
    private final static String EXCLUDE_DEPRECATED_KEY = "excludeDeprecated";
    private CodeReferencesFeature codeReferencesFeature;

    @Override
    public String id() {
        return "java-enum-entries";
    }

    @Override
    public IncludePlugin create() {
        return new JavaEnumEntriesIncludePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .add("title", PluginParamType.STRING, "title to use for parameters block", "\"myMethod parameters\"")
                .add(JavaDocMarkdownParameter.definition)
                .add(EXCLUDE_DEPRECATED_KEY, PluginParamType.BOOLEAN, "exclude deprecated entries from the list", "true")
                .add(CodeReferencesFeature.paramsDefinition);
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        codeReferencesFeature = new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams);
        features.add(codeReferencesFeature);

        ApiParameters apiParameters = new ApiParameters(determineAnchorPrefix());
        javaCode.getEnumEntries().stream()
                .filter(this::includeEnum)
                .forEach((enumEntry) -> {
                    JavaDocElementsMapsAndSearchText elementsMapsAndSearchText = javaDocTextToDocElements(
                            enumEntry.getJavaDocText(), codeReferencesFeature);
                    apiParameters.add(enumEntry.getName(), new ApiLinkedText(), elementsMapsAndSearchText.docElementsMaps,
                            elementsMapsAndSearchText.searchText);
        });

        Map<String, Object> props = apiParameters.toMap();
        features.updateProps(props);
        props.putAll(pluginParams.getOpts().toMap());

        List<DocElement> docElements =
                PluginResult.docElement("ApiParameters", props).getDocElements();

        return new JavaIncludeResult(docElements, extractText(javaCode.getEnumEntries()));
    }

    private boolean includeEnum(EnumEntry enumEntry) {
        boolean excludeDeprecated = pluginParams.getOpts().get("excludeDeprecated", false);
        return !(excludeDeprecated && enumEntry.isDeprecated());
    }

    private String extractText(List<EnumEntry> enums) {
        return enums.stream().map(e -> e.getName() + " " + e.getJavaDocText())
                .collect(joining(" "));
    }

    private String determineAnchorPrefix() {
        if (!entries.isEmpty()) {
            return entries.get(0);
        }

        return fullPath.getFileName().toString();
    }
}
