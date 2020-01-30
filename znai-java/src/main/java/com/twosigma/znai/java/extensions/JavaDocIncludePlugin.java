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

import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.java.parser.JavaCode;
import com.twosigma.znai.java.parser.html.HtmlToDocElementConverter;

public class JavaDocIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-doc";
    }

    @Override
    public IncludePlugin create() {
        return new JavaDocIncludePlugin();
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        String entry = pluginParams.getOpts().get("entry");

        return new JavaIncludeResult(HtmlToDocElementConverter.convert(componentsRegistry, markupPath,
                entry == null ? javaCode.getClassJavaDocText() : javaCode.findJavaDoc(entry),
                codeReferencesTrait.getReferences()), entry);
    }
}
