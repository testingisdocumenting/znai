/*
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
