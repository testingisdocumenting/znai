package com.twosigma.documentation.parser.sphinx;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author mykola
 */
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

    public static String getAttributeText(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getTextContent();
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
