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

import com.twosigma.znai.codesnippets.CodeSnippetsProps;
import com.twosigma.znai.extensions.PluginParamsOpts;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.java.parser.JavaCode;
import com.twosigma.znai.java.parser.JavaMethod;
import com.twosigma.znai.java.parser.JavaType;
import com.twosigma.znai.parser.docelement.DocElement;
import com.twosigma.znai.parser.docelement.DocElementType;
import com.twosigma.znai.utils.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.znai.java.parser.JavaCodeUtils.removeReturn;
import static com.twosigma.znai.java.parser.JavaCodeUtils.removeSemicolonAtEnd;

public class JavaIncludePlugin extends JavaIncludePluginBase {
    private PluginParamsOpts opts;
    private boolean isBodyOnly;
    private boolean isSignatureOnly;
    private boolean isMultipleEntries;

    @Override
    public String id() {
        return "java";
    }

    @Override
    public IncludePlugin create() {
        return new JavaIncludePlugin();
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        opts = pluginParams.getOpts();

        isBodyOnly = opts.get("bodyOnly", false);
        isSignatureOnly = opts.get("signatureOnly", false);
        isMultipleEntries = !entries.isEmpty();

        if (isBodyOnly && isSignatureOnly) {
            throw new IllegalArgumentException("specify only bodyOnly or signatureOnly");
        }

        String snippet = extractContent(javaCode);
        Map<String, Object> props = CodeSnippetsProps.create("java", snippet);
        props.putAll(pluginParams.getOpts().toMap());
        codeReferences.updateProps(props);

        DocElement docElement = new DocElement(DocElementType.SNIPPET);
        props.forEach(docElement::addProp);

        return new JavaIncludeResult(Collections.singletonList(docElement), snippet);
    }

    private String extractContent(JavaCode javaCode) {
        if (entry == null && entries.isEmpty()) {
            return javaCode.getFileContent();
        }

        Stream<String> methodNamesWithOptionalTypes = entry != null ? Stream.of(entry) : entries.stream();
        return methodNamesWithOptionalTypes
                .map(nameWithOptionalType -> extractSingleEntryContent(javaCode, nameWithOptionalType))
                .collect(collectorWithSeparator());
    }

    private String extractSingleEntryContent(JavaCode javaCode, String entry) {
        return javaCode.hasType(entry) ?
                extractTypeContent(javaCode, entry) :
                extractMethodContent(javaCode, entry);
    }

    private String extractTypeContent(JavaCode javaCode, String entry) {
        JavaType type = javaCode.findType(entry);
        return isBodyOnly ? type.getBodyOnly() : type.getFullBody();
    }

    private String extractMethodContent(JavaCode javaCode, String entry) {
        List<JavaMethod> methods = isMultipleEntries ?
                javaCode.findAllMethods(entry) :
                Collections.singletonList(javaCode.findMethod(entry));

        if (isBodyOnly) {
            return extractBodiesOnly(methods);
        }

        if (isSignatureOnly) {
            return extractSignaturesOnly(methods);
        }

        return extractFullBodies(methods);
    }

    private String extractFullBodies(List<JavaMethod> methods) {
        return methods.stream()
                .map(JavaMethod::getFullBody)
                .collect(collectorWithSeparator());
    }

    private String extractBodiesOnly(List<JavaMethod> methods) {
        return methods.stream()
                .map(this::extractBodyOnly)
                .collect(collectorWithSeparator());
    }

    private String extractSignaturesOnly(List<JavaMethod> methods) {
        return methods.stream()
                .map(JavaMethod::getSignatureOnly)
                .collect(collectorWithSeparator());
    }

    private String extractBodyOnly(JavaMethod method) {
        String result = method.getBodyOnly();

        boolean removeReturn = opts.get("removeReturn", false);
        result = removeReturn ? StringUtils.stripIndentation(removeReturn(result)) : result;

        boolean removeSemicolon = opts.get("removeSemicolon", false);
        result = removeSemicolon ? removeSemicolonAtEnd(result) : result;

        return result;
    }

    private Collector<CharSequence, ?, String> collectorWithSeparator() {
        return Collectors.joining(isSignatureOnly ? "\n" : "\n\n");
    }
}
