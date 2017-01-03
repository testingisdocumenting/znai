package com.twosigma.documentation.parser.docelement;

import java.util.*;

import com.twosigma.documentation.extensions.IncludeParams;
import com.twosigma.documentation.extensions.IncludePlugin;
import com.twosigma.documentation.extensions.IncludePlugins;
import com.twosigma.documentation.extensions.ReactComponent;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.utils.CollectionUtils;

/**
 * @author mykola
 */
public class DocElementCreationParserHandler implements ParserHandler {
    private DocElement docElement;
    private Deque<DocElement> elementsStack;

    public DocElementCreationParserHandler() {
        docElement = new DocElement("page");
        elementsStack = new ArrayDeque<>();
        elementsStack.add(docElement);
    }

    public DocElement getDocElement() {
        return docElement;
    }

    @Override
    public void onSectionStart(String title) {
        start(DocElementType.SECTION, "title", title);
    }

    @Override
    public void onSectionEnd() {
        end();
    }

    @Override
    public void onParagraphStart() {
        start(DocElementType.PARAGRAPH);
    }

    @Override
    public void onParagraphEnd() {
        end();
    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {
        start(DocElementType.BULLET_LIST, "bulletMarker", bulletMarker, "tight", tight);
    }

    @Override
    public void onBulletListEnd() {
        end();
    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {
        start(DocElementType.ORDERED_LIST, "delimiter", delimiter, "startNumber", startNumber);
    }

    @Override
    public void onOrderedListEnd() {
        end();
    }

    @Override
    public void onListItemStart() {
        start(DocElementType.LIST_ITEM);
    }

    @Override
    public void onListItemEnd() {
        end();
    }

    @Override
    public void onEmphasisStart() {
        start(DocElementType.EMPHASIS);
    }

    @Override
    public void onEmphasisEnd() {
        end();
    }

    @Override
    public void onStrongEmphasisStart() {
        start(DocElementType.STRONG_EMPHASIS);
    }

    @Override
    public void onStrongEmphasisEnd() {
        end();
    }

    @Override
    public void onSimpleText(String value) {
        append(DocElementType.SIMPLE_TEXT, "text", value);
    }

    @Override
    public void onLink(String label, String anchor) {
        append(DocElementType.LINK, "label", label, "anchor", anchor);
    }

    @Override
    public void onSnippet(String lang, String lineNumber, String snippet) {
        append(DocElementType.SNIPPET, "lang", lang, "lineNumber", lineNumber, "snippet", snippet);
    }

    @Override
    public void onThematicBreak() {
        append(DocElementType.THEMATIC_BREAK);
    }

    @Override
    public void onInclude(final String pluginId, final String value) {
        final IncludePlugin includePlugin = IncludePlugins.byId(pluginId);
        final ReactComponent reactComponent = includePlugin.process(new IncludeParams(value, Collections.emptyMap()));// TODO custom params

        DocElement customComponent = new DocElement(DocElementType.CUSTOM_COMPONENT);
        customComponent.addProp("componentName", reactComponent.getName());
        customComponent.addProp("componentProps", reactComponent.getProps());

        append(customComponent);
    }

    private void start(String type, Object... propsKeyValue) {
        DocElement element = new DocElement(type);
        addProps(element, propsKeyValue);

        appendAndPush(element);
    }

    private void end() {
        elementsStack.removeLast();
    }

    private void append(String type, Object... propsKeyValue) {
        DocElement element = new DocElement(type);
        addProps(element, propsKeyValue);

        append(element);
    }

    private void append(DocElement element) {
        elementsStack.peekLast().addChild(element);
    }

    private void addProps(DocElement element, Object... propsKeyValue) {
        Map<String, Object> props = CollectionUtils.createMap(propsKeyValue);
        props.forEach(element::addProp);
    }

    private void appendAndPush(DocElement element) {
        elementsStack.peekLast().addChild(element);
        elementsStack.add(element);
    }
}

