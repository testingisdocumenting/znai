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
import org.testingisdocumenting.znai.java.parser.JavaCode;
import org.testingisdocumenting.znai.java.parser.JavaMethod;
import org.testingisdocumenting.znai.java.parser.JavaMethodReturn;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class JavaDocParamsIncludePlugin extends JavaIncludePluginBase {
    private CodeReferencesFeature codeReferencesFeature;

    @Override
    public String id() {
        return "java-doc-params";
    }

    @Override
    public IncludePlugin create() {
        return new JavaDocParamsIncludePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .addRequired(ENTRY_KEY, PluginParamType.STRING, "entry to extract parameters definition from",
                        "\"myMethod\"")
                .add("title", PluginParamType.STRING, "title to use for parameters block", "\"myMethod parameters\"")
                .add(CodeReferencesFeature.paramsDefinition);
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        codeReferencesFeature = new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams);
        features.add(codeReferencesFeature);

        JavaMethod javaMethod = javaCode.findMethod(entries.get(0));

        ApiParameters apiParameters = new ApiParameters(javaMethod.getAnchorPrefix());

        addReturn(apiParameters, javaMethod);
        javaMethod.getParams().forEach(param -> {
            JavaDocElementsMapsAndSearchText docElementsMapsAndSearchText =
                    javaDocTextToDocElements(param.getJavaDocText(), codeReferencesFeature);
            apiParameters.add(param.getName(), new ApiLinkedText(param.getType()),
                    docElementsMapsAndSearchText.docElementsMaps, docElementsMapsAndSearchText.searchText);
        });

        Map<String, Object> props = apiParameters.toMap();
        features.updateProps(props);
        props.putAll(pluginParams.getOpts().toMap());

        List<DocElement> docElements =
                PluginResult.docElement("ApiParameters", props).getDocElements();

        return new JavaIncludeResult(docElements, extractText(javaMethod));
    }

    private String extractText(JavaMethod javaMethod) {
        JavaMethodReturn methodReturn = javaMethod.getJavaMethodReturn();

        String returnPart = methodReturn != null ?
                "return " + methodReturn.getType() + " " + methodReturn.getJavaDocText():
                "";
        String paramsPart = javaMethod.getParams().stream()
                .map(p -> p.getName() + " " + p.getType() + " " + p.getJavaDocText())
                .collect(joining(" "));

        return paramsPart +
                (returnPart.isEmpty() ? "" : " ") +
                returnPart;
    }

    private void addReturn(ApiParameters apiParameters, JavaMethod javaMethod) {
        JavaMethodReturn methodReturn = javaMethod.getJavaMethodReturn();
        if (methodReturn == null) {
            return;
        }

        JavaDocElementsMapsAndSearchText elementsMapsAndSearchText =
                javaDocTextToDocElements(methodReturn.getJavaDocText(), codeReferencesFeature);

        apiParameters.add("return", new ApiLinkedText(methodReturn.getType()),
                elementsMapsAndSearchText.docElementsMaps, elementsMapsAndSearchText.searchText);
    }
}
