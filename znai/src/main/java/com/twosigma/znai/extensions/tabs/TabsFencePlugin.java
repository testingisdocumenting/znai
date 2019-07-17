package com.twosigma.znai.extensions.tabs;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.ColonDelimitedKeyValues;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.search.SearchScore;
import com.twosigma.znai.search.SearchText;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class TabsFencePlugin implements FencePlugin {
    private Path markupPath;
    private MarkupParser parser;

    private List<String> texts;

    @Override
    public String id() {
        return "tabs";
    }

    @Override
    public FencePlugin create() {
        return new TabsFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        this.markupPath = markupPath;
        this.parser = componentsRegistry.defaultParser();
        this.texts = new ArrayList<>();

        ColonDelimitedKeyValues tabsDefinitions = new ColonDelimitedKeyValues(content);
        List<ParsedTab> parsedTabs = tabsDefinitions.map(this::parseTab).collect(toList());

        Map<String, Object> tabsProps = new LinkedHashMap<>(pluginParams.getOpts().toMap());
        tabsProps.put("tabsContent", parsedTabs.stream().map(this::tabProps).collect(toList()));

        parsedTabs.forEach(this::generateSearchText);

        return PluginResult.docElement("Tabs", tabsProps);
    }

    private Map<String, Object> tabProps(ParsedTab parsedTab) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("name", parsedTab.name);
        props.put("content", parsedTab.parserResult.contentToListOfMaps());

        return props;
    }

    private void generateSearchText(ParsedTab parsedTab) {
        texts.add(parsedTab.name);
        texts.add(parsedTab.parserResult.getAllText());
    }

    private ParsedTab parseTab(String tabName, String markup) {
        MarkupParserResult parserResult = parser.parse(markupPath, markup);
        return new ParsedTab(tabName, parserResult);
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(String.join(" ", texts));
    }

    private static class ParsedTab {
        private String name;
        private MarkupParserResult parserResult;

        private ParsedTab(String name, MarkupParserResult parserResult) {
            this.name = name;
            this.parserResult = parserResult;
        }
    }
}
