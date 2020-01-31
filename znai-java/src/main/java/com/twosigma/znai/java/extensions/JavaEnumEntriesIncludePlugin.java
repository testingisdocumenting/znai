/*
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

package com.twosigma.znai.java.extensions;

import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.api.ApiParameters;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.java.parser.EnumEntry;
import com.twosigma.znai.java.parser.JavaCode;
import com.twosigma.znai.java.parser.html.HtmlToDocElementConverter;
import com.twosigma.znai.parser.docelement.DocElement;
import com.twosigma.znai.parser.docelement.DocElementType;

import java.util.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class JavaEnumEntriesIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-enum-entries";
    }

    @Override
    public IncludePlugin create() {
        return new JavaEnumEntriesIncludePlugin();
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        ApiParameters apiParameters = new ApiParameters();
        javaCode.getEnumEntries().stream()
                .filter(this::includeEnum)
                .forEach((enumEntry) -> {
            apiParameters.add(enumEntry.getName(), "", javaDocTextToDocElements(enumEntry.getJavaDocText()));
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

    private List<Map<String, Object>> nameToDocElements(EnumEntry e) {
        return Collections.singletonList(
                new DocElement(DocElementType.INLINED_CODE, "code", e.getName()).toMap());
    }

    private List<Map<String, Object>> descriptionToDocElements(EnumEntry e) {
        return HtmlToDocElementConverter.convert(
                componentsRegistry, markupPath, e.getJavaDocText(),
                codeReferencesFeature.getReferences())
                .stream()
                .map(DocElement::toMap).collect(toList());
    }
}
