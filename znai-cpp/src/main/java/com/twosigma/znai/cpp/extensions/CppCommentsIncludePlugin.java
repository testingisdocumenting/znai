package com.twosigma.znai.cpp.extensions;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.cpp.parser.CodePart;
import com.twosigma.znai.cpp.parser.CppSourceCode;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginParamsOpts;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CppCommentsIncludePlugin implements IncludePlugin {
    private Path cppPath;

    @Override
    public String id() {
        return "cpp-comments";
    }

    @Override
    public IncludePlugin create() {
        return new CppCommentsIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String fileName = pluginParams.getFreeParam();
        cppPath = componentsRegistry.resourceResolver().fullPath(fileName);
        String text = componentsRegistry.resourceResolver().textContent(fileName);

        String comments = extractComments(text, pluginParams.getOpts());
        MarkupParserResult parserResult = componentsRegistry.defaultParser().parse(cppPath, comments);
        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(cppPath));
    }

    private String extractComments(String text, PluginParamsOpts opts) {
        String entry = opts.getRequiredString("entry");
        String body = CppSourceCode.entryBodyOnly(text, entry);

        List<CodePart> parts = CppSourceCode.splitOnComments(body);
        return parts.stream().filter(CodePart::isComment)
                .map(cp -> cp.getData().trim())
                .filter(c -> c.startsWith("@mdoc"))
                .map(c -> c.replaceAll("^@mdoc", "").trim())
                .collect(Collectors.joining("\n\n"));
    }

}
