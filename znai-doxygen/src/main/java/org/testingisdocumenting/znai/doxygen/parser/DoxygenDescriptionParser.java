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

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Node;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DoxygenDescriptionParser {
    private final Node descriptionRoot;
    private final List<String> textParts;

    private DocElementCreationParserHandler parserHandler;

    private DoxygenDescriptionParser(Node descriptionNode) {
        this.descriptionRoot = descriptionNode;
        this.textParts = new ArrayList<>();
    }

    public static DoxygenDescription parse(ComponentsRegistry componentsRegistry, Node descriptionNode) {
        DoxygenDescriptionParser parser = new DoxygenDescriptionParser(descriptionNode);
        return parser.parseXml(componentsRegistry);
    }

    private DoxygenDescription parseXml(ComponentsRegistry componentsRegistry) {
        parserHandler = new DocElementCreationParserHandler(componentsRegistry, Paths.get("doxygen-xml"));

        parseChildren(descriptionRoot);
        parserHandler.onParsingEnd();

        return new DoxygenDescription(parserHandler.getDocElement().getContent(),
                String.join(" ", textParts));
    }

    private void parseChildren(Node node) {
        XmlUtils.forEach(node.getChildNodes(), this::handleDescriptionNode);
    }

    private void handleDescriptionNode(Node node) {
        if (node.getNodeName().equals("para")) {
            parserHandler.onParagraphStart();
            parseChildren(node);
            parserHandler.onParagraphEnd();
        } else if (node.getNodeName().equals("bold")) {
            parserHandler.onStrongEmphasisStart();
            parseChildren(node);
            parserHandler.onStrongEmphasisEnd();
        } else if (node.getNodeName().equals("emphasis")) {
            parserHandler.onEmphasisStart();
            parseChildren(node);
            parserHandler.onEmphasisEnd();
        } else if (node.getNodeName().equals("itemizedlist")) {
            parserHandler.onBulletListStart('*', false);
            parseChildren(node);
            parserHandler.onBulletListEnd();
        } else if (node.getNodeName().equals("orderedlist")) {
            parserHandler.onOrderedListStart(' ', 1);
            parseChildren(node);
            parserHandler.onOrderedListEnd();
        } else if (node.getNodeName().equals("listitem")) {
            parserHandler.onListItemStart();
            parseChildren(node);
            parserHandler.onListItemEnd();
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            String textContent = node.getTextContent();
            String textContentTrimmed = textContent.trim();
            if (!textContentTrimmed.isEmpty()) {
                parserHandler.onSimpleText(textContent);
                textParts.add(textContentTrimmed);
            }
        }
    }
}
