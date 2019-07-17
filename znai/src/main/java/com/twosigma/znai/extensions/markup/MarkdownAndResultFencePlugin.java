package com.twosigma.znai.extensions.markup;

import com.twosigma.znai.codesnippets.CodeSnippetsProps;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class MarkdownAndResultFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "markdown-and-result";
    }

    @Override
    public FencePlugin create() {
        return new MarkdownAndResultFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkupParser parser = componentsRegistry.defaultParser();
        MarkupParserResult parserResult = parser.parse(markupPath, content);

        Map<String, Object> markdown = CodeSnippetsProps.create("markdown", content);
        markdown.put("type", DocElementType.SNIPPET);

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("markdown", markdown);
        props.put("result", parserResult.contentToListOfMaps());

        return PluginResult.docElement("MarkdownAndResult", props);
    }
}
