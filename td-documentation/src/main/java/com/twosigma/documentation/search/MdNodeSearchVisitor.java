package com.twosigma.documentation.search;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Text;

import com.twosigma.documentation.html.HtmlAnchors;
import com.twosigma.documentation.structure.TocItem;

/**
 * @author mykola
 */
public class MdNodeSearchVisitor extends AbstractVisitor {
    private LunrIndex lunrIndex;
    private TocItem tocItem;
    private String currentTopLevelHeader;

    public MdNodeSearchVisitor(LunrIndex lunrIndex, TocItem tocItem) {
        this.lunrIndex = lunrIndex;
        this.tocItem = tocItem;
        this.currentTopLevelHeader = "";

        lunrIndex.addTitle(generatePageId(), generatePageIdToDisplay(), tocItem.getPageTitle());
    }

    @Override
    public void visit(final Heading heading) {
        if (heading.getLevel() == 1) {
            currentTopLevelHeader = ((Text) heading.getFirstChild()).getLiteral();
            lunrIndex.addHeader(generateHeaderId(), generateHeaderIdToDisplay(), currentTopLevelHeader);
        } else {
            super.visit(heading);
        }
    }

    @Override
    public void visit(final IndentedCodeBlock indentedCodeBlock) {
        lunrIndex.addCode(generateHeaderId(), generateHeaderIdToDisplay(), indentedCodeBlock.getLiteral());
    }

    @Override
    public void visit(final Text text) {
        lunrIndex.addBody(generateHeaderId(),
            generateHeaderIdToDisplay(),
            text.getLiteral());

        super.visit(text);
    }

    private String generatePageIdToDisplay() {
        return tocItem.getSectionTitle() + ", " + tocItem.getPageTitle();
    }

    private String generateHeaderIdToDisplay() {
        return generatePageIdToDisplay() + " " + currentTopLevelHeader;
    }

    private String generatePageId() {
        return tocItem.getDirName() + "/" + tocItem.getFileNameWithoutExtension() + ".html#";
    }

    private String generateHeaderId() {
        return generatePageId() + HtmlAnchors.headingAnchor(currentTopLevelHeader);
    }
}
