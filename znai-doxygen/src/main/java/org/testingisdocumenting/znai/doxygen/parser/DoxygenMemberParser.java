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
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DoxygenMemberParser {
    private final ComponentsRegistry componentsRegistry;
    private final String content;
    private final String compoundId;
    private final String id;
    private final DoxygenMember member;

    private DoxygenMemberParser(ComponentsRegistry componentsRegistry, String content, String compoundId, String id) {
        this.componentsRegistry = componentsRegistry;
        this.content = content;
        this.compoundId = compoundId;
        this.id = id;
        this.member = new DoxygenMember();
    }

    public static DoxygenMember parse(ComponentsRegistry componentsRegistry, String content, String compoundId, String id) {
        DoxygenMemberParser parser = new DoxygenMemberParser(componentsRegistry, content, compoundId, id);
        parser.parseXml();

        return parser.member;
    }

    private void parseXml() {
        Document document = XmlUtils.parseXml(content);
        Node root = XmlUtils.nextLevelNodeByName(document, "doxygen");
        Node compoundRoot = XmlUtils.childrenNodesStreamByName(root, "compounddef")
                .filter(node -> compoundId.equals(XmlUtils.getAttributeText(node, "id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find compound with id: " + compoundId));

        XmlUtils.childrenNodesStreamByName(compoundRoot, "sectiondef")
                .forEach(this::parseSection);
    }

    private void parseSection(Node sectionNode) {
        Node memberNode = XmlUtils.childrenNodesStreamByName(sectionNode, "memberdef")
                .filter(node -> id.equals(XmlUtils.getAttributeText(node, "id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find memberdef with id: " + id));

        parseMember(memberNode);
    }

    private void parseMember(Node memberNode) {
        member.definition = XmlUtils.anyNestedNodeByName(memberNode, "definition").getTextContent();
        member.description = DoxygenDescriptionParser.parse(componentsRegistry,
                id,
                XmlUtils.anyNestedNodeByName(memberNode, "detaileddescription"));
    }
}
