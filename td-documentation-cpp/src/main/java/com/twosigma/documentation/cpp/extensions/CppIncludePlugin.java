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
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class CppIncludePlugin implements IncludePlugin {
    private MarkupParser markupParser;
    private Path cppPath;
    private CodeTokenizer codeTokenizer;
    private String fileName;

    @Override
    public String id() {
        return "cpp";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        markupParser = componentsRegistry.parser();
        codeTokenizer = componentsRegistry.codeTokenizer();
        fileName = includeParams.getFreeParam();
        cppPath = componentsRegistry.includeResourceResolver().fullPath(this.fileName);

        IncludeParamsOpts opts = includeParams.getOpts();
        String commentsType = opts.has("comments") ? opts.get("comments") : "";

        String text = componentsRegistry.includeResourceResolver().textContent(fileName);

        String snippet = extractSnippet(text, opts);

        List<CodePart> codeParts = commentsType.equals("inline") ?
                CppSourceCode.splitOnComments(snippet):
                Collections.singletonList(new CodePart(false, snippet));

        return PluginResult.docElements(codeParts.stream().flatMap(this::convertToDocElement));
    }

    private Stream<DocElement> convertToDocElement(CodePart codePart) {
        return codePart.isComment() ?
                parseComments(codePart.getData()) :
                createSnippet(codePart.getData());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(cppPath));
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
        String entry = opts.get("entry");
        if (entry == null) {
            return text;
        }

        Boolean bodyOnly = opts.get("bodyOnly");
        return bodyOnly != null && bodyOnly ?
                CppSourceCode.entryBodyOnly(text, entry):
                CppSourceCode.entryDefinition(text, entry);
    }
}
