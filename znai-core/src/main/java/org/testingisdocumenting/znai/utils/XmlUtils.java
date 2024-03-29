/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.utils;

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
        NodeList matched = allNestedNodesListByName(parent, name);
        return matched.getLength() > 0;
    }

    public static Node anyNestedNodeByName(Node parent, String name) {
        NodeList matched = allNestedNodesListByName(parent, name);

        if (matched.getLength() == 0) {
            throw new IllegalArgumentException("expected to find element <" + name + ">");
        }

        return matched.item(0);
    }

    public static Node nextLevelNodeByName(Node parent, String name) {
        return childrenNodesStreamByName(parent, name)
                .findFirst()
                .orElseThrow(() ->  new IllegalArgumentException("expected to find element <" + name + ">"));
    }

    public static void forEach(NodeList list, Consumer<Node> consumer) {
        int len = list.getLength();
        for (int i = 0; i < len; i++) {
            consumer.accept(list.item(i));
        }
    }

    public static Stream<Node> allNestedNodesStreamByName(Node parent, String name) {
        NodeList matched = allNestedNodesListByName(parent, name);

        Stream.Builder<Node> streamBuilder = Stream.builder();
        forEach(matched, streamBuilder);

        return streamBuilder.build();
    }

    public static Stream<Node> childrenNodesStreamByName(Node parent, String name) {
        Stream.Builder<Node> streamBuilder = Stream.builder();

        NodeList childNodes = parent.getChildNodes();
        for (int idx = 0; idx < childNodes.getLength(); idx++) {
            Node node = childNodes.item(idx);
            if (node.getNodeName().equals(name)) {
                streamBuilder.accept(node);
            }
        }

        return streamBuilder.build();
    }

    public static boolean hasAttributeText(Node node, String name) {
        return node.getAttributes().getNamedItem(name) != null;
    }

    public static String getAttributeText(Node node, String name) {
        Node namedItem = node.getAttributes().getNamedItem(name);
        return namedItem != null ? namedItem.getTextContent() : null;
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

    private static NodeList allNestedNodesListByName(Node parent, String name) {
        return (parent instanceof Document) ?
                ((Document) parent).getElementsByTagName(name):
                ((Element) parent).getElementsByTagName(name);
    }
}
