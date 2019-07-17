package com.twosigma.znai.extensions.xml;

import com.twosigma.znai.utils.XmlUtils;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class XmlToMapRepresentationConverter extends DefaultHandler {
    private final List<Map<String, Object>> result;
    private final Deque<List<Map<String, Object>>> currentStack;
    private StringBuilder accumulatedText;

    public static Map<String, ?> convert(String xmlContent) {
        return new XmlToMapRepresentationConverter().convertXml(xmlContent);
    }

    private XmlToMapRepresentationConverter() {
        currentStack = new ArrayDeque<>();
        result = new ArrayList<>();

        currentStack.add(result);
        accumulatedText = new StringBuilder();
    }

    private Map<String, ?> convertXml(String xmlContent) {
        XmlUtils.parseXml(xmlContent, this);
        return result.get(0);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        Map<String, Object> asMap = new LinkedHashMap<>();

        List<Map<String, Object>> children = new ArrayList<>();
        asMap.put("tagName", qName);
        asMap.put("attributes", parseAttributes(attributes));
        asMap.put("children", children);

        currentStack.peekLast().add(asMap);
        currentStack.add(children);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        accumulatedText.append(new String(ch, start, length));
    }

    private Map<String, Object> createTextNode(String text) {
        Map<String, Object> textNode = new LinkedHashMap<>();
        textNode.put("tagName", "");
        textNode.put("text", text);

        return textNode;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (! accumulatedText.toString().trim().isEmpty()) {
            currentStack.peekLast().add(createTextNode(accumulatedText.toString().trim()));
        }

        currentStack.removeLast();
        accumulatedText = new StringBuilder();
    }

    private List<Map<String, Object>> parseAttributes(Attributes attributes) {
        List<Map<String, Object>> result = new ArrayList<>();

        int len = attributes.getLength();
        for (int i = 0; i < len; i++) {
            String name = attributes.getLocalName(i);
            String value = attributes.getValue(i);

            Map<String, Object> attr = new LinkedHashMap<>();
            attr.put("name", name);
            attr.put("value", "\"" + value + "\"");

            result.add(attr);
        }

        return result;
    }
}
