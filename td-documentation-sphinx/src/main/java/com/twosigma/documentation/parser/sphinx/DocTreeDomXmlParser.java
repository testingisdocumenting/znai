package com.twosigma.documentation.parser.sphinx;

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

    private void parseNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            parseNode(item);
        }
    }

    private void parseNode(Node node) {
        switch (node.getNodeName()) {
            case "document":
                parseNodeList(node.getChildNodes());
                break;
            case "section":
                parserHandler.onSectionStart(extractTitle(node));
                parseNodeList(node.getChildNodes());
                parserHandler.onSectionEnd();
                break;
            case "paragraph":
                parserHandler.onParagraphStart();
                parseNodeList(node.getChildNodes());
                parserHandler.onParagraphEnd();
                break;
            case "title":
                break;
            case "#text":
                if (! node.getTextContent().trim().isEmpty()) {
                    parserHandler.onSimpleText(node.getTextContent());
                }
                break;
        }
    }

    private String extractTitle(Node node) {
        Element element = (Element) node;
        return element.getElementsByTagName("title").item(0).getTextContent();
    }
}
