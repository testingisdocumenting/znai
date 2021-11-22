/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.doxygen.parser;

import org.apache.commons.io.FilenameUtils;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Node;

public class DoxygenProgramListingParser {
    private final Node node;
    private final StringBuilder codeBlock;
    private final String fileName;

    private DoxygenProgramListingParser(Node node) {
        this.node = node;
        this.codeBlock = new StringBuilder();
        this.fileName = XmlUtils.getAttributeText(node, "filename", "");
    }

    public static DoxygenCodeBlockSimple parseAsSimpleCodeBlock(Node node) {
        DoxygenProgramListingParser parser = new DoxygenProgramListingParser(node);
        return parser.parse();
    }

    private DoxygenCodeBlockSimple parse() {
        XmlUtils.childrenNodesStreamByName(node, "codeline").forEach(this::handleLine);
        return new DoxygenCodeBlockSimple(FilenameUtils.getExtension(fileName), codeBlock.toString());
    }

    private void handleLine(Node lineNode) {
        XmlUtils.forEach(lineNode.getChildNodes(), this::extractText);

        codeBlock.append("\n");
    }

    private void extractText(Node node) {
        if (node.getNodeName().equals("sp")) {
            codeBlock.append(" ");
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            codeBlock.append(node.getTextContent());
        }

        XmlUtils.forEach(node.getChildNodes(), this::extractText);
    }
}
