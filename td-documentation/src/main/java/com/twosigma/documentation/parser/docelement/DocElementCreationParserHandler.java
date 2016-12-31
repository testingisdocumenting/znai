package com.twosigma.documentation.parser.docelement;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

import com.twosigma.documentation.extensions.IncludeParams;
import com.twosigma.documentation.extensions.IncludePlugin;
import com.twosigma.documentation.extensions.IncludePlugins;
import com.twosigma.documentation.extensions.ReactComponent;
import com.twosigma.documentation.parser.ParserHandler;

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
        DocElement section = new DocElement(DocElementType.SECTION);
        section.addProp("title", title);

        appendAndPush(section);
    }

    @Override
    public void onSectionEnd() {
        elementsStack.removeLast();
    }

    @Override
    public void onParagraphStart() {
        DocElement paragraph = new DocElement(DocElementType.PARAGRAPH);
        appendAndPush(paragraph);
    }

    @Override
    public void onParagraphEnd() {
        elementsStack.removeLast();
    }

    public void onEmphasisStart() {
        DocElement emphasis = new DocElement(DocElementType.EMPHASIS);
        appendAndPush(emphasis);
    }

    public void onEmphasisEnd() {
        elementsStack.removeLast();
    }

    public void onStrongEmphasisStart() {
        DocElement strongEmphasis = new DocElement(DocElementType.STRONG_EMPHASIS);
        appendAndPush(strongEmphasis);
    }

    public void onStrongEmphasisEnd() {
        elementsStack.removeLast();
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

    private void append(DocElement element) {
        elementsStack.peekLast().addChild(element);
    }

    private void appendAndPush(DocElement element) {
        elementsStack.peekLast().addChild(element);
        elementsStack.add(element);
    }
}

