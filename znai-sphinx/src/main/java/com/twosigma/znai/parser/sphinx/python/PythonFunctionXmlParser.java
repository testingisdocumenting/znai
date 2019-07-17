package com.twosigma.znai.parser.sphinx.python;

import com.twosigma.znai.utils.XmlUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.Collectors;

public class PythonFunctionXmlParser {
    private static final String PARAM_TYPE_NODE_NAME = "literal_emphasis";

    private PythonFunction function;

    public PythonFunction parse(Node desc) {
        Node descSignature = XmlUtils.nodeByName(desc, "desc_signature");
        Node descName = XmlUtils.nodeByName(descSignature, "desc_name");
        Node descContent = XmlUtils.nodeByName(desc, "desc_content");
        Node paragraph = XmlUtils.nodeByName(descContent, "paragraph");

        function = new PythonFunction(XmlUtils.getAttributeText(descSignature, "ids"),
                descName.getTextContent(), paragraph.getTextContent());

        parseSignature(descSignature);

        XmlUtils.nodesStreamByName(descContent, "field")
                .forEach(this::parseFields);

        return function;
    }

    private void parseFields(Node node) {
        Node fieldName = XmlUtils.nodeByName(node, "field_name");
        if (fieldName.getTextContent().equals("Parameters")) {
            XmlUtils.nodesStreamByName(node, "list_item").forEach(this::parseParam);
        }
    }

    private void parseSignature(Node descSignature) {
        Node paramsList = XmlUtils.nodeByName(descSignature, "desc_parameterlist");
        NodeList childNodes = paramsList.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            boolean isOptional = node.getNodeName().equals("desc_optional");

            function.addParamSignature(new PythonFunctionParamSignature(node.getTextContent(), isOptional));
        }
    }

    private void parseParam(Node listItem) {
        String paramName = XmlUtils.nodeByName(listItem, "literal_strong").getTextContent();
        String paramType = parseParamType(listItem);

        Node paragraph = XmlUtils.nodeByName(listItem, "paragraph");
        String description = extractParamDesc(paragraph);

        function.addParam(new PythonFunctionParam(paramName, paramType, description));
    }

    private static String parseParamType(Node listItem) {
        return XmlUtils.nodesStreamByName(listItem, PARAM_TYPE_NODE_NAME)
                .map(Node::getTextContent).collect(Collectors.joining(" "));
    }

    private static String extractParamDesc(Node paragraph) {
        String remainingText = paragraph.getLastChild().getTextContent().trim();

        if (remainingText.startsWith(")")) {
            return remainingText.replaceAll("^\\) – ", "");
        }

        return remainingText.replaceAll("^– ", "");
    }
}
