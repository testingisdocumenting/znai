package com.twosigma.documentation.extensions.file;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.inlinedcode.InlinedCodePlugin;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

public class FileInlinedCodePlugin implements InlinedCodePlugin {
    private String fileName;
    private String text;

    @Override
    public String id() {
        return "file";
    }

    @Override
    public InlinedCodePlugin create() {
        return new FileInlinedCodePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();

        text = FilePlugin.extractText(
                componentsRegistry.resourceResolver().textContent(fileName),
                pluginParams.getOpts());


        return PluginResult.docElement(DocElementType.INLINED_CODE, Collections.singletonMap("code", text));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.resourceResolver().fullPath(fileName)));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(text);
    }
}
