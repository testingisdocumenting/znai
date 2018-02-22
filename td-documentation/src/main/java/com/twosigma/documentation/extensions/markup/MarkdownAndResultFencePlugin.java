package com.twosigma.documentation.extensions.markup;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class MarkdownAndResultFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "markdown-and-result";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkupParser parser = componentsRegistry.defaultParser();
        MarkupParserResult parserResult = parser.parse(markupPath, content);

        Map<String, Object> markdown = CodeSnippetsProps.create(componentsRegistry.codeTokenizer(), "markdown", content);
        markdown.put("type", DocElementType.SNIPPET);

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("markdown", markdown);
        props.put("result", parserResult.contentToListOfMaps());

        return PluginResult.docElement("MarkdownAndResult", props);
    }
}
