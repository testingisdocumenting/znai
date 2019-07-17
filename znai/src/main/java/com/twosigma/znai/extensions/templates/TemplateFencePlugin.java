package com.twosigma.znai.extensions.templates;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.ColonDelimitedKeyValues;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;
import com.twosigma.znai.template.TextTemplate;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

public class TemplateFencePlugin implements FencePlugin {
    private Path fullPath;
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "template";
    }

    @Override
    public FencePlugin create() {
        return new TemplateFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();

        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());
        parserResult = parser.parse(markupPath, processTemplate(resourcesResolver.textContent(fullPath),
                new ColonDelimitedKeyValues(content)));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    private String processTemplate(String template, ColonDelimitedKeyValues keyValues) {
        return new TextTemplate(fullPath.getFileName().toString(), template)
                .process(Collections.unmodifiableMap(keyValues.toMap()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(parserResult.getAuxiliaryFiles().stream(),
                Stream.of(AuxiliaryFile.builtTime(fullPath)));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(parserResult.getAllText());
    }
}
