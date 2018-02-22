package com.twosigma.documentation.extensions.templates;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginParamsOpts;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;
import com.twosigma.documentation.template.TextTemplate;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TemplateIncludePlugin implements IncludePlugin {
    private Path fullPath;
    private MarkupParserResult parserResult;
    private ResourcesResolver resourcesResolver;
    private Path paramsPath;

    @Override
    public String id() {
        return "template";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();

        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());
        parserResult = parser.parse(markupPath, processTemplate(resourcesResolver.textContent(fullPath),
                pluginParams.getOpts()));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    private String processTemplate(String template, PluginParamsOpts opts) {
        Map<String, Object> params = new HashMap<>(opts.toMap());

        if (opts.has("paramsPath")) {
            paramsPath = resourcesResolver.fullPath(opts.get("paramsPath"));
            params.putAll(JsonUtils.deserializeAsMap(FileUtils.fileTextContent(paramsPath)));
        }

        return new TextTemplate(fullPath.getFileName().toString(), template).process(params);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        Stream<AuxiliaryFile> files = Stream.concat(parserResult.getAuxiliaryFiles().stream(),
                Stream.of(AuxiliaryFile.builtTime(fullPath)));

        return (paramsPath != null) ?
            Stream.concat(Stream.of(AuxiliaryFile.builtTime(paramsPath)), files):
            files;
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(parserResult.getAllText());
    }
}
