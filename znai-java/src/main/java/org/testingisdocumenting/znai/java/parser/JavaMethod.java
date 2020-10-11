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

package org.testingisdocumenting.znai.java.parser;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class JavaMethod {
    private final String name;
    private final String nameWithTypes;
    private final String fullBody;
    private final String bodyOnly;
    private final String signatureOnly;
    private final String javaDocText;
    private final List<JavaMethodParam> params;
    private final JavaMethodReturn javaMethodReturn;
    private final String anchorPrefix;

    public JavaMethod(String name, String fullBody, String bodyOnly, String signatureOnly,
                      List<JavaMethodParam> params,
                      JavaMethodReturn javaMethodReturn,
                      String javaDocText) {
        this.name = name;
        this.nameWithTypes = name + "(" + params.stream().map(JavaMethodParam::getType).collect(joining(",")) + ")";
        this.fullBody = fullBody;
        this.bodyOnly = bodyOnly;
        this.signatureOnly = signatureOnly;
        this.javaDocText = javaDocText;
        this.params = params;
        this.javaMethodReturn = javaMethodReturn != null && javaMethodReturn.getType().equals("void") ? null : javaMethodReturn;
        this.anchorPrefix = buildAnchorPrefix();
    }

    public String getName() {
        return name;
    }

    public String getNameWithTypes() {
        return nameWithTypes;
    }

    public String getFullBody() {
        return fullBody;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }

    public String getJavaDocText() {
        return javaDocText;
    }

    public String getSignatureOnly() {
        return signatureOnly;
    }

    public List<JavaMethodParam> getParams() {
        return params;
    }

    public JavaMethodReturn getJavaMethodReturn() {
        return javaMethodReturn;
    }

    public List<String> getParamNames() {
        return params.stream().map(JavaMethodParam::getName).collect(toList());
    }

    public String getAnchorPrefix() {
        return anchorPrefix;
    }

    private String buildAnchorPrefix() {
        String fromParams = params.stream().map(p -> p.getName() + "_" + p.getType())
                .collect(joining("_"));
        return name + (fromParams.isEmpty() ? "" : "_" + fromParams);
    }
}
