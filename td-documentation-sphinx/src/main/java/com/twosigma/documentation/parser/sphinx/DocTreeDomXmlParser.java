package com.twosigma.documentation.parser.sphinx;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.parser.ParserHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.twosigma.documentation.parser.sphinx.XmlUtils.parseXml;

/**
 * @author mykola
 */
class DocTreeDomXmlParser {
    private ParserHandler parserHandler;

    public DocTreeDomXmlParser(ParserHandler parserHandler) {
        this.parserHandler = parserHandler;
    }

    public void parse(String xml) {
        Document document = parseXml(xml);
        NodeList nodes = document.getChildNodes();

        parseNodeList(nodes);
    }

    private boolean parseNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            parseNode(item);
        }

        return true;
    }

    private boolean parseNode(Node node) {
        switch (node.getNodeName()) {
            case "document":
                return parseChildren(node);
            case "section":
                return parseSection(node);
            case "paragraph":
                return parseParagraph(node);
            case "emphasis":
                return parseEmphasis(node);
            case "strong":
                return parseStrong(node);
            case "literal_block":
                return parseSnippet(node);
            case "title":
                return false;
            case "#text":
                return parseText(node);
        }

        return false;
    }

    private boolean parseEmphasis(Node node) {
        parserHandler.onEmphasisStart();
        parseChildren(node);
        parserHandler.onEmphasisEnd();

        return true;
    }

    private boolean parseStrong(Node node) {
        parserHandler.onStrongEmphasisStart();
        parseChildren(node);
        parserHandler.onStrongEmphasisEnd();

        return true;
    }

    private boolean parseText(Node node) {
        parserHandler.onSimpleText(node.getTextContent());
        return true;
    }

    private boolean parseSnippet(Node node) {
        parserHandler.onSnippet(PluginParams.EMPTY, getAttributeText(node, "language"),
                "", node.getTextContent());

        return true;
    }

    private boolean parseParagraph(Node node) {
        parserHandler.onParagraphStart();
        parseChildren(node);
        parserHandler.onParagraphEnd();

        return true;
    }

    private boolean parseSection(Node node) {
        parserHandler.onSectionStart(extractTitle(node));
        parseChildren(node);
        parserHandler.onSectionEnd();

        return true;
    }

    private boolean parseChildren(Node node) {
        return parseNodeList(node.getChildNodes());
    }

    private String getAttributeText(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getTextContent();
    }

    private String extractTitle(Node node) {
        Element element = (Element) node;
        return element.getElementsByTagName("title").item(0).getTextContent();
    }
}
