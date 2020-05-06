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

package org.testingisdocumenting.znai.parser.table;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownVisitor;
import org.commonmark.ext.gfm.tables.*;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.CustomNode;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

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
        MarkdownVisitor markdownVisitor = new MarkdownVisitor(componentsRegistry, markdownPath, handler);

        bodyCell.accept(markdownVisitor);

        return (List<Map<String, Object>>) handler.getDocElement().toMap().get("content");
    }
}
