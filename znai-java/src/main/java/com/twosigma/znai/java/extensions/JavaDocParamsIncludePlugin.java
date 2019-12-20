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
import com.twosigma.znai.java.parser.JavaCode;
import com.twosigma.znai.java.parser.JavaMethod;
import com.twosigma.znai.java.parser.JavaMethodReturn;
import com.twosigma.znai.java.parser.html.HtmlToDocElementConverter;
import com.twosigma.znai.parser.docelement.DocElement;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class JavaDocParamsIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-doc-params";
    }

    @Override
    public IncludePlugin create() {
        return new JavaDocParamsIncludePlugin();
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        JavaMethod javaMethod = javaCode.findMethod(entry);

        ApiParameters apiParameters = new ApiParameters();
        addReturn(apiParameters, javaMethod);
        javaMethod.getParams().forEach(param -> {
            apiParameters.add(param.getName(), param.getType(), javaDocTextToDocElements(param.getJavaDocText()));
        });

        Map<String, Object> props = apiParameters.toMap();
        codeReferences.updateProps(props);

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

        apiParameters.add("return", methodReturn.getType(),
                javaDocTextToDocElements(methodReturn.getJavaDocText()));
    }

    private List<Map<String, Object>> javaDocTextToDocElements(String text) {
        return HtmlToDocElementConverter.convert(componentsRegistry, markupPath, text).stream()
                .map(DocElement::toMap)
                .collect(toList());
    }
}
