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
        DocElement text = new DocElement(DocElementType.SIMPLE_TEXT);
        text.addProp("text", value);
        append(text);
    }

    @Override
    public void onLink(String label, String anchor) {
        DocElement link = new DocElement(DocElementType.LINK); // TODO cross pages?
        link.addProp("label", label);
        link.addProp("anchor", anchor);

        append(link);
    }

    @Override
    public void onSnippet(String lang, String lineNumber, String snippet) {
        DocElement sourceCode = new DocElement(DocElementType.SNIPPET);
        sourceCode.addProp("lang", lang);
        sourceCode.addProp("lineNumber", lineNumber);
        sourceCode.addProp("snippet", snippet);

        append(sourceCode);
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
        Map<String, Object> props = CollectionUtils.createMap(propsKeyValue);
        props.forEach(element::addProp);

        appendAndPush(element);
    }

    private void end() {
        elementsStack.removeLast();
    }

    private void append(DocElement element) {
        elementsStack.peekLast().addChild(element);
    }

    private void appendAndPush(DocElement element) {
        elementsStack.peekLast().addChild(element);
        elementsStack.add(element);
    }
}

