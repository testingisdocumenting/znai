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

    public PythonClass parse(String xml) {
        Document document = parseXml(xml);
        return parseClass(document);
    }

    public PythonClass parseClass(Node node) {
        Node descSignature = nodeByName(node, "desc_signature");
        Node descContent = nodeByName(node, "desc_content");
        Node description = nodeByName(descContent, "paragraph");

        pythonClass = new PythonClass(XmlUtils.getAttributeText(descSignature, "ids"),
                getAttributeText(descSignature, "fullname"),
                description.getTextContent());
        
        XmlUtils.nodesStreamByName(descContent, "desc").forEach(this::parseMethod);
        return pythonClass;
    }

    private void parseMethod(Node desc) {
        PythonFunction method = new PythonFunctionXmlParser().parse(desc);
        pythonClass.addMethod(method);
    }
}
