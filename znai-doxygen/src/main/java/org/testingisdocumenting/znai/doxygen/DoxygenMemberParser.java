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
import org.w3c.dom.Node;

public class DoxygenMemberParser {
    private final String content;
    private final String compoundId;
    private final String id;
    private final DoxygenMember member;

    private DoxygenMemberParser(String content, String compoundId, String id) {
        this.content = content;
        this.compoundId = compoundId;
        this.id = id;
        this.member = new DoxygenMember();
    }

    public static DoxygenMember parse(String content, String compoundId, String id) {
        DoxygenMemberParser parser = new DoxygenMemberParser(content, compoundId, id);
        parser.parseXml();

        return parser.member;
    }

    private void parseXml() {
        Document document = XmlUtils.parseXml(content);
        Node root = XmlUtils.nodeByName(document, "doxygen");
        Node compoundRoot = XmlUtils.nodesStreamByName(root, "compounddef")
                .filter(node -> compoundId.equals(XmlUtils.getAttributeText(node, "id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find compound with id: " + compoundId));

        XmlUtils.nodesStreamByName(compoundRoot, "sectiondef")
                .forEach(this::parseSection);
    }

    private void parseSection(Node sectionNode) {
        Node memberNode = XmlUtils.nodesStreamByName(sectionNode, "memberdef")
                .filter(node -> id.equals(XmlUtils.getAttributeText(node, "id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find memberdef with id: " + id));

        parseMember(memberNode);
    }

    private void parseMember(Node memberNode) {
        member.definition = XmlUtils.nodeByName(memberNode, "definition").getTextContent();
    }
}
