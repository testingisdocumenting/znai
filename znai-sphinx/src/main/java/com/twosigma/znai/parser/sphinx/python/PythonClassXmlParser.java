package com.twosigma.znai.parser.sphinx.python;

import com.twosigma.znai.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class PythonClassXmlParser {
    private PythonClass pythonClass;

    public PythonClass parse(String xml) {
        Document document = XmlUtils.parseXml(xml);
        return parseClass(document);
    }

    public PythonClass parseClass(Node node) {
        Node descSignature = XmlUtils.nodeByName(node, "desc_signature");
        Node descContent = XmlUtils.nodeByName(node, "desc_content");
        Node description = XmlUtils.nodeByName(descContent, "paragraph");

        pythonClass = new PythonClass(XmlUtils.getAttributeText(descSignature, "ids"),
                XmlUtils.getAttributeText(descSignature, "fullname"),
                description.getTextContent());
        
        XmlUtils.nodesStreamByName(descContent, "desc").forEach(this::parseMethod);
        return pythonClass;
    }

    private void parseMethod(Node desc) {
        PythonFunction method = new PythonFunctionXmlParser().parse(desc);
        pythonClass.addMethod(method);
    }
}
