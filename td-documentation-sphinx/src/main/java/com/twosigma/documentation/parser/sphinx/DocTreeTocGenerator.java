package com.twosigma.documentation.parser.sphinx;

import com.twosigma.documentation.structure.TableOfContents;
import com.twosigma.documentation.structure.TocGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author mykola
 */
public class DocTreeTocGenerator implements TocGenerator {
    @Override
    public TableOfContents generate(String indexXml) {
        TableOfContents toc = new TableOfContents();

        Document doc = XmlUtils.parseXml(indexXml);
        Element compound = (Element) doc.getElementsByTagName("compound").item(0);

        NodeList references = compound.getElementsByTagName("reference");
        for (int i = 0; i < references.getLength(); i++) {
            Node node = references.item(i);
            String uri = node.getAttributes().getNamedItem("refuri").getTextContent();
            toc.addTocItem("NONAME", uri);
        }

        return toc;
    }
}
