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
    private final TableOfContents toc;

    public DocTreeTocGenerator() {
        toc = new TableOfContents();
    }

    @Override
    public TableOfContents generate(String indexXml) {
        Document doc = XmlUtils.parseXml(indexXml);
        Element firstBulletList = (Element) doc.getElementsByTagName("bullet_list").item(0);

        NodeList chapterNodes = firstBulletList.getChildNodes();
        for (int chapterIdx = 0; chapterIdx < chapterNodes.getLength(); chapterIdx++) {
            Node node = chapterNodes.item(chapterIdx);

            if (node.getNodeType() == Node.TEXT_NODE) {
                continue;
            }

            handleChapter(node);
        }

        toc.addIndex();
        return toc;
    }

    private void handleChapter(Node node) {
        Element element = (Element) node;
        NodeList nestedBulletList = element.getElementsByTagName("bullet_list");
        if (nestedBulletList.getLength() == 0) {
            return;
        }

        Element nestedBulletListElement = (Element) nestedBulletList.item(0);
        Node chapterReference = element.getElementsByTagName("reference").item(0);

        NodeList pages = nestedBulletListElement.getElementsByTagName("reference");

        String sectionTitle = chapterReference.getTextContent();
        for (int pageIdx = 0; pageIdx < pages.getLength(); pageIdx++) {
            Node page = pages.item(pageIdx);
            handlePage(sectionTitle, page);
        }
    }

    private void handlePage(String sectionTitle, Node node) {
        String dirAndFileName = node.getAttributes().getNamedItem("refuri").getTextContent();
        String[] parts = dirAndFileName.split("[/\\\\]");
        if (parts.length != 2) {
            return;
        }

        toc.addTocItem(parts[0], parts[1], sectionTitle);
    }
}
