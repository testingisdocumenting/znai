package com.twosigma.documentation.extensions.templates;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.*;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;
import com.twosigma.utils.RegexpUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.twosigma.documentation.extensions.templates.Template.VARIABLE_PATTERN;

/**
 * @author mykola
 */
public class TemplateIncludePlugin implements IncludePlugin {
    private Path fullPath;
    private MarkupParserResult parserResult;
    private PluginResourcesResolver resourcesResolver;
    private Path paramsPath;

    @Override
    public String id() {
        return "template";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        resourcesResolver = componentsRegistry.includeResourceResolver();
        MarkupParser parser = componentsRegistry.parser();

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

        return RegexpUtils.replaceAll(template, VARIABLE_PATTERN,
                (matcher) -> {
                    String paramName = matcher.group(1);
                    if (! params.containsKey(paramName)) {
                        throw new RuntimeException("no parameter '" + paramName + "' found: " + params);
                    }

                    return params.get(paramName).toString();
                });
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        Stream<AuxiliaryFile> files = Stream.concat(parserResult.getAuxiliaryFiles().stream(),
                Stream.of(AuxiliaryFile.builtTime(fullPath)));

        return (paramsPath != null) ?
            Stream.concat(Stream.of(AuxiliaryFile.builtTime(paramsPath)), files):
            files;
    }
}
