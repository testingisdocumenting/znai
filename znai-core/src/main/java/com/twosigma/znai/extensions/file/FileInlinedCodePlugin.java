package com.twosigma.znai.extensions.file;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.inlinedcode.InlinedCodePlugin;
import com.twosigma.znai.parser.docelement.DocElementType;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

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
