package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.extensions.PluginParamsOpts;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.java.parser.JavaMethod;
import com.twosigma.documentation.java.parser.JavaType;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.utils.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.documentation.java.parser.JavaCodeUtils.removeReturn;
import static com.twosigma.documentation.java.parser.JavaCodeUtils.removeSemicolonAtEnd;

/**
 * @author mykola
 */
public class JavaIncludePlugin extends JavaIncludePluginBase {
    private PluginParamsOpts opts;

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

        Boolean bodyOnly = opts.get("bodyOnly", false);
        Boolean signatureOnly = opts.get("signatureOnly", false);

        if (bodyOnly && signatureOnly) {
            throw new IllegalArgumentException("specify only bodyOnly or signatureOnly");
        }

        String snippet = extractContent(javaCode, bodyOnly, signatureOnly);
        Map<String, Object> props = CodeSnippetsProps.create("java", snippet);
        props.putAll(pluginParams.getOpts().toMap());

        DocElement docElement = new DocElement(DocElementType.SNIPPET);
        props.forEach(docElement::addProp);

        return new JavaIncludeResult(Collections.singletonList(docElement), snippet);
    }

    private String extractContent(JavaCode javaCode, Boolean isBodyOnly, Boolean isSignatureOnly) {
        if (entry == null && entries == null) {
            return javaCode.getFileContent();
        }

        Stream<String> methodNamesWithOptionalTypes = entry != null ? Stream.of(entry) : entries.stream();
        return methodNamesWithOptionalTypes.map(n -> extractSingleContent(javaCode, n, isBodyOnly, isSignatureOnly))
                .collect(Collectors.joining(isSignatureOnly ? "\n" : "\n\n"));
    }

    private String extractSingleContent(JavaCode javaCode, String entry, Boolean isBodyOnly, Boolean isSignatureOnly) {
        return javaCode.hasType(entry) ?
                extractTypeContent(javaCode, entry, isBodyOnly) :
                extractMethodContent(javaCode, entry, isBodyOnly, isSignatureOnly);
    }

    private String extractTypeContent(JavaCode javaCode, String entry, Boolean isBodyOnly) {
        JavaType type = javaCode.findType(entry);
        return isBodyOnly ? type.getBodyOnly() : type.getFullBody();
    }

    private String extractMethodContent(JavaCode javaCode, String entry, Boolean isBodyOnly, Boolean isSignatureOnly) {
        JavaMethod method = javaCode.findMethod(entry);

        return isBodyOnly ?
                extractBodyOnly(method) :
                isSignatureOnly ? method.getSignatureOnly() :
                        method.getFullBody();

    }

    private String extractBodyOnly(JavaMethod method) {
        String result = method.getBodyOnly();

        boolean removeReturn = opts.get("removeReturn", false);
        result = removeReturn ? StringUtils.stripIndentation(removeReturn(result)) : result;

        boolean removeSemicolon = opts.get("removeSemicolon", false);
        result = removeSemicolon ? removeSemicolonAtEnd(result) : result;

        return result;
    }
}
