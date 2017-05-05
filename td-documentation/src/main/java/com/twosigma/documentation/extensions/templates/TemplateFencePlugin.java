package com.twosigma.documentation.extensions.templates;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResourcesResolver;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.utils.RegexpUtils;

import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TemplateFencePlugin implements FencePlugin {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$(\\w+)");
    private Path fullPath;
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "template";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        PluginResourcesResolver resourcesResolver = componentsRegistry.includeResourceResolver();
        MarkupParser parser = componentsRegistry.parser();

        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());
        parserResult = parser.parse(markupPath, processTemplate(resourcesResolver.textContent(fullPath),
                new TemplateKeyValues(content)));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    private String processTemplate(String template, TemplateKeyValues keyValues) {
        return RegexpUtils.replaceAll(template, VARIABLE_PATTERN,
                (matcher) -> keyValues.get(matcher.group(1)));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(parserResult.getAuxiliaryFiles().stream(),
                Stream.of(AuxiliaryFile.builtTime(fullPath)));
    }
}
