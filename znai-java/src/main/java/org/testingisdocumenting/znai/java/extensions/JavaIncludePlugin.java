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

import org.testingisdocumenting.znai.codesnippets.CodeSnippetsProps;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.file.*;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.java.parser.JavaCode;
import org.testingisdocumenting.znai.java.parser.JavaMethod;
import org.testingisdocumenting.znai.java.parser.JavaType;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;
import org.testingisdocumenting.znai.utils.EntriesSeparatorUtils;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.testingisdocumenting.znai.java.parser.JavaCodeUtils.removeReturn;
import static org.testingisdocumenting.znai.java.parser.JavaCodeUtils.removeSemicolonAtEnd;

public class JavaIncludePlugin extends JavaIncludePluginBase {
    private PluginParamsOpts opts;
    private boolean isBodyOnly;
    private boolean isSignatureOnly;
    private String entrySeparator;
    private ManipulatedSnippetContentProvider contentProvider;

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
        entrySeparator = opts.get("entrySeparator");

        if (isBodyOnly && isSignatureOnly) {
            throw new IllegalArgumentException("specify only bodyOnly or signatureOnly");
        }

        contentProvider = new ManipulatedSnippetContentProvider(path,
                extractContent(javaCode),
                pluginParams);

        features.add(new SnippetAutoTitleFeature(contentProvider));
        features.add(new SnippetHighlightFeature(componentsRegistry, pluginParams, contentProvider));
        features.add(new SnippetRevealLineStopFeature(pluginParams, contentProvider));

        Map<String, Object> props = CodeSnippetsProps.create("java", contentProvider.snippetContent());
        props.putAll(pluginParams.getOpts().toMap());
        features.updateProps(props);

        DocElement docElement = new DocElement(DocElementType.SNIPPET);
        props.forEach(docElement::addProp);

        return new JavaIncludeResult(Collections.singletonList(docElement), contentProvider.snippetContent());
    }

    private String extractContent(JavaCode javaCode) {
        if (entries.isEmpty()) {
            return javaCode.getFileContent();
        }

        return entries.stream()
                .map(nameWithOptionalType -> extractEntryContent(javaCode, nameWithOptionalType))
                .collect(collectorWithSeparator());
    }

    private String extractEntryContent(JavaCode javaCode, String entry) {
        return javaCode.hasType(entry) ?
                extractTypeContent(javaCode, entry) :
                extractMethodContent(javaCode, entry);
    }

    private String extractTypeContent(JavaCode javaCode, String entry) {
        JavaType type = javaCode.findType(entry);
        return isBodyOnly ? type.getBodyOnly() : type.getFullBody();
    }

    private String extractMethodContent(JavaCode javaCode, String entry) {
        List<JavaMethod> methods = javaCode.findAllMethods(entry);

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
        String separator = entrySeparator;
        if (separator == null) {
            separator = isSignatureOnly ? "" : "\n";
        }

        return Collectors.joining(EntriesSeparatorUtils.enrichUserTextEntriesSeparator(separator));
    }
}
