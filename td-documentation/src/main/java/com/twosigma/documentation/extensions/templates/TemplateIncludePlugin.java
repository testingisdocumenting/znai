package com.twosigma.documentation.extensions.templates;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.*;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.utils.RegexpUtils;

import java.nio.file.Path;
import java.util.stream.Stream;

import static com.twosigma.documentation.extensions.templates.Template.VARIABLE_PATTERN;

/**
 * @author mykola
 */
public class TemplateIncludePlugin implements IncludePlugin {
    private Path fullPath;
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "template";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        PluginResourcesResolver resourcesResolver = componentsRegistry.includeResourceResolver();
        MarkupParser parser = componentsRegistry.parser();

        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());
        parserResult = parser.parse(markupPath, processTemplate(resourcesResolver.textContent(fullPath),
                pluginParams.getOpts()));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    private String processTemplate(String template, PluginParamsOpts opts) {
        return RegexpUtils.replaceAll(template, VARIABLE_PATTERN,
                (matcher) -> opts.get(matcher.group(1)));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(parserResult.getAuxiliaryFiles().stream(),
                Stream.of(AuxiliaryFile.builtTime(fullPath)));
    }
}
