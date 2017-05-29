package com.twosigma.documentation.parser.table;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;
import com.twosigma.documentation.parser.docelement.DocElementVisitor;
import org.commonmark.ext.gfm.tables.*;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author mykola
 */
public class GfmTableToTableConverter extends AbstractVisitor {
    private ComponentsRegistry componentsRegistry;
    private Path markdownPath;

    private MarkupTableData tableData;
    private Row row;
    private boolean isHeader;

    public GfmTableToTableConverter(ComponentsRegistry componentsRegistry, Path markdownPath, TableBlock tableBlock) {
        this.componentsRegistry = componentsRegistry;
        this.markdownPath = markdownPath;
        this.tableData = new MarkupTableData();

        tableBlock.accept(this);
    }

    public MarkupTableData convert() {
        return tableData;
    }

    @Override
    public void visit(CustomNode customNode) {
        if (customNode instanceof TableHead) {
            isHeader = true;
            visitChildren(customNode);
        } else if (customNode instanceof TableBody) {
            isHeader = false;
            visitChildren(customNode);
        } else if (customNode instanceof TableRow) {
            row = new Row();
            visitChildren(customNode);
            if (! isHeader) {
                tableData.addRow(row);
            }
        } else if (customNode instanceof TableCell) {
            if (isHeader) {
                handleHeaderCell((TableCell) customNode);
            } else {
                handleBodyCell((TableCell) customNode);
            }
        } else {
            throw new UnsupportedOperationException("can't handle: " + customNode);
        }
    }

    private void handleHeaderCell(TableCell headerCell) {
        tableData.addColumn(extractTitle(headerCell), extractAlignment(headerCell));
    }

    private String extractTitle(TableCell headerCell) {
        List<Map<String, Object>> docElements = contentFromCell(headerCell);
        return docElements.get(0).get("text").toString();
    }

    private String extractAlignment(TableCell headerCell) {
        if (headerCell == null || headerCell.getAlignment() == null) {
            return "left";
        }

        switch (headerCell.getAlignment()) {
            case CENTER:
                return "center";
            case RIGHT:
                return "right";
            default:
                return "left";
        }
    }

    private void handleBodyCell(TableCell bodyCell) {
        Object content = contentFromCell(bodyCell);
        row.add(content);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> contentFromCell(TableCell bodyCell) {
        DocElementCreationParserHandler handler = new DocElementCreationParserHandler(componentsRegistry, markdownPath);
        DocElementVisitor docElementVisitor = new DocElementVisitor(componentsRegistry, markdownPath, handler);

        bodyCell.accept(docElementVisitor);

        return (List<Map<String, Object>>) handler.getDocElement().toMap().get("content");
    }
}
