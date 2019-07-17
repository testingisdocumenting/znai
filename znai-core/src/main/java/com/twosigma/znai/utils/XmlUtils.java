package com.twosigma.znai.utils;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class XmlUtils {
    private XmlUtils() {
    }

    public static Document parseXml(String xml) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setValidating(false);

            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));

            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8.name())));
            doc.getDocumentElement().normalize();

            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void parseXml(String xmlContent, DefaultHandler elementHandler) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);

            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)), elementHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasNodeByName(Node parent, String name) {
        NodeList matched = nodesListByName(parent, name);
        return matched.getLength() > 0;
    }

    public static Node nodeByName(Node parent, String name) {
        NodeList matched = nodesListByName(parent, name);

        if (matched.getLength() == 0) {
            throw new IllegalArgumentException("expected to find element <" + name + ">");
        }

        return matched.item(0);
    }

    public static void forEach(NodeList list, Consumer<Node> consumer) {
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            consumer.accept(list.item(i));
        }
    }

    public static Stream<Node> nodesStreamByName(Node parent, String name) {
        NodeList matched = nodesListByName(parent, name);

        Stream.Builder<Node> streamBuilder = Stream.builder();
        forEach(matched, streamBuilder);

        return streamBuilder.build();
    }

    public static boolean hasAttributeText(Node node, String name) {
        return node.getAttributes().getNamedItem(name) != null;
    }

    public static String getAttributeText(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getTextContent();
    }

    public static String getAttributeText(Node node, String name, String defaultValue) {
        Node namedItem = node.getAttributes().getNamedItem(name);
        return namedItem == null ? defaultValue : namedItem.getTextContent();
    }

    public static Map<String, String> getAttributes(Node node) {
        Map<String, String> result = new LinkedHashMap<>();

        NamedNodeMap attributes = node.getAttributes();
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            Node item = attributes.item(i);
            result.put(item.getNodeName(), item.getTextContent());
        }

        return result;
    }

    private static NodeList nodesListByName(Node parent, String name) {
        return (parent instanceof Document) ?
                ((Document) parent).getElementsByTagName(name):
                ((Element) parent).getElementsByTagName(name);
    }
}
