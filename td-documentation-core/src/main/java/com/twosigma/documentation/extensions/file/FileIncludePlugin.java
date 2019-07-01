package com.twosigma.documentation.extensions.file;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class FileIncludePlugin implements IncludePlugin {
    private String fileName;
    private String text;

    @Override
    public String id() {
        return "file";
    }

    @Override
    public IncludePlugin create() {
        return new FileIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();

        text = FilePlugin.extractText(
                componentsRegistry.resourceResolver().textContent(fileName),
                pluginParams.getOpts());

        String providedLang = pluginParams.getOpts().getString("lang");
        String langToUse = (providedLang == null) ? langFromFileName(fileName) : providedLang;

        Map<String, Object> props = CodeSnippetsProps.create(langToUse, text);
        props.putAll(pluginParams.getOpts().toMap());

        return PluginResult.docElement(DocElementType.SNIPPET, props);
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


    private static String langFromFileName(String fileName) {
        String ext = extFromFileName(fileName);
        switch (ext) {
            case "js": return "javascript";
            default: return ext;
        }
    }

    private static String extFromFileName(String fileName) {
        int dotLastIdx = fileName.lastIndexOf('.');
        if (dotLastIdx == -1) {
            return "";
        }

        return fileName.substring(dotLastIdx + 1);
    }


}
