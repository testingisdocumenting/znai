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

package org.testingisdocumenting.znai.doxygen;

import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DoxygenIndexParser {
    private final String content;
    private final DoxygenIndex index;
    private Document document;
    private Node indexNode;
    private DoxygenIndexCompound currentCompound;

    private DoxygenIndexParser(String content) {
        this.content = content;
        this.index = new DoxygenIndex();
    }

    public static DoxygenIndex parse(String content) {
        DoxygenIndexParser parser = new DoxygenIndexParser(content);
        parser.parseXml();

        return parser.index;
    }

    private void parseXml() {
        document = XmlUtils.parseXml(content);
        indexNode = findRoot();
        parseCompoundNodes();
    }

    private void parseCompoundNodes() {
        XmlUtils.nodesStreamByName(indexNode, "compound")
                .forEach((compoundNode) -> {
                    NamedNodeMap attributes = compoundNode.getAttributes();
                    currentCompound = new DoxygenIndexCompound(
                            attributes.getNamedItem("refid").getTextContent(),
                            attributes.getNamedItem("kind").getTextContent(),
                            DoxygenXmlUtils.extractNameNodeText(compoundNode));

                    extractMembers(compoundNode);
                });
    }

    private void extractMembers(Node compoundNode) {
        XmlUtils.nodesStreamByName(compoundNode, "member")
                .forEach((memberNode) -> {
                    NamedNodeMap attributes = memberNode.getAttributes();
                    DoxygenIndexMember member = new DoxygenIndexMember(
                            currentCompound,
                            attributes.getNamedItem("refid").getTextContent(),
                            attributes.getNamedItem("kind").getTextContent(),
                            DoxygenXmlUtils.extractNameNodeText(memberNode));

                    index.add(member);
                });
    }

    private Node findRoot() {
        return XmlUtils.nodeByName(document, "doxygenindex");
    }
}
