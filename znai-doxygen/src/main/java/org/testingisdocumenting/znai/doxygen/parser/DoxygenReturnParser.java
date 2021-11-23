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
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Node;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DoxygenReturnParser {
    private final ComponentsRegistry componentsRegistry;
    private final List<String> textParts;

    private DocElementCreationParserHandler parserHandler;

    private DoxygenReturnParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
        this.textParts = new ArrayList<>();
    }

    public static DoxygenReturn parse(ComponentsRegistry componentsRegistry, Node node) {
        DoxygenReturnParser parser = new DoxygenReturnParser(componentsRegistry);
        return parser.parse(node);
    }

    private DoxygenReturn parse(Node node) {
        parserHandler = new DocElementCreationParserHandler(componentsRegistry, Paths.get("doxygen-xml"));
        parseChildren(node);
        parserHandler.onParsingEnd();

        return new DoxygenReturn(parserHandler.getDocElement().getContent(),
                String.join(" ", textParts));
    }

    private void parseChildren(Node node) {
        XmlUtils.forEach(node.getChildNodes(), this::handleRichTextNode);
    }

    private void handleRichTextNode(Node node) {
        String nodeName = node.getNodeName();
        if (nodeName.equals("para")) {
            parserHandler.onParagraphStart();
            parseChildren(node);
            parserHandler.onParagraphEnd();
        } else if (nodeName.equals("computeroutput")) {
            String textContent = node.getTextContent();
            parserHandler.onInlinedCode(textContent, DocReferences.EMPTY);
            textParts.add(textContent);
        } else if (nodeName.equals("bold")) {
            parserHandler.onStrongEmphasisStart();
            parseChildren(node);
            parserHandler.onStrongEmphasisEnd();
        } else if (nodeName.equals("emphasis")) {
            parserHandler.onEmphasisStart();
            parseChildren(node);
            parserHandler.onEmphasisEnd();
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
