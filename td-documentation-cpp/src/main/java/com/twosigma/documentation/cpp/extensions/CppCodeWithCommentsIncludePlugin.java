package com.twosigma.documentation.cpp.extensions;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.cpp.parser.CodePart;
import com.twosigma.documentation.cpp.parser.CppSourceCode;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludeParamsOpts;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludePluginResult;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class CppCodeWithCommentsIncludePlugin implements IncludePlugin {
    private MarkupParser markupParser;
    private Path cppPath;
    private CodeTokenizer codeTokenizer;

    @Override
    public String id() {
        return "cpp-with-comments";
    }

    @Override
    public IncludePluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        this.markupParser = componentsRegistry.parser();
        this.codeTokenizer = componentsRegistry.codeTokenizer();
        this.cppPath = componentsRegistry.includeResourceResolver().fullPath(includeParams.getFreeParam());

        String fileName = includeParams.getFreeParam();
        String text = componentsRegistry.includeResourceResolver().textContent(fileName);
        String snippet = extractSnippet(text, includeParams.getOpts());

        List<CodePart> codeParts = CppSourceCode.splitOnComments(snippet);

        return IncludePluginResult.docElements(codeParts.stream().flatMap(this::convertToDocElement));
    }

    private Stream<DocElement> convertToDocElement(CodePart codePart) {
        return codePart.isComment() ?
                parseComments(codePart.getData()) :
                createSnippet(codePart.getData());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry, IncludeParams includeParams) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.includeResourceResolver().fullPath(includeParams.getFreeParam())));
    }

    private Stream<DocElement> parseComments(String data) {
        MarkupParserResult parserResult = markupParser.parse(cppPath, data);
        return parserResult.getDocElement().getContent().stream();
    }

    private Stream<DocElement> createSnippet(String snippet) {
        DocElement docElement = new DocElement(DocElementType.SNIPPET);
        Map<String, Object> props = CodeSnippetsProps.create(codeTokenizer, "cpp", snippet);
        props.forEach(docElement::addProp);

        return Stream.of(docElement);
    }

    private String extractSnippet(String text, IncludeParamsOpts opts) {
        String method = opts.get("method");
        if (method == null) {
            return text;
        }

        return CppSourceCode.methodBody(text, method);
    }
}
