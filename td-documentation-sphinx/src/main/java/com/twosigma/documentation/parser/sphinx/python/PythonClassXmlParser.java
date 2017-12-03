package com.twosigma.documentation.parser.sphinx.python;

import com.twosigma.documentation.parser.sphinx.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static com.twosigma.documentation.parser.sphinx.XmlUtils.getAttributeText;
import static com.twosigma.documentation.parser.sphinx.XmlUtils.nodeByName;
import static com.twosigma.documentation.parser.sphinx.XmlUtils.parseXml;

/**
 * @author mykola
 */
public class PythonClassXmlParser {
    private PythonClass pythonClass;
    private PythonMethod method;

    public PythonClass parse(String xml) {
        Document document = parseXml(xml);
        parseClass(document);

        return pythonClass;
    }

    private void parseClass(Node node) {
        Node descSignature = nodeByName(node, "desc_signature");
        Node descContent = nodeByName(node, "desc_content");
        Node description = nodeByName(descContent, "paragraph");

        pythonClass = new PythonClass(getAttributeText(descSignature, "fullname"), description.getTextContent());

        XmlUtils.nodesStreamByName(descContent, "desc").forEach(this::parseMethod);
    }

    private void parseMethod(Node desc) {
        Node descSignature = XmlUtils.nodeByName(desc, "desc_signature");
        Node descName = XmlUtils.nodeByName(descSignature, "desc_name");
        Node descContent = XmlUtils.nodeByName(desc, "desc_content");
        Node paragraph = XmlUtils.nodeByName(descContent, "paragraph");

        method = new PythonMethod(descName.getTextContent(), paragraph.getTextContent());
        pythonClass.addMethod(method);

        XmlUtils.nodesStreamByName(descContent, "list_item").forEach(this::parseParam);
    }

    private void parseParam(Node listItem) {
        String paramName = XmlUtils.nodeByName(listItem, "literal_strong").getTextContent();
        Node paragraph = XmlUtils.nodeByName(listItem, "paragraph");
        String paramDesc = paragraph.getLastChild().getTextContent().trim().replace("– ", "");

        PythonMethodParam param = new PythonMethodParam(paramName, paramDesc);
        method.addParam(param);
    }
}
