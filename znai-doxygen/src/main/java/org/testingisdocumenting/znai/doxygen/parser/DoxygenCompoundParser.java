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

public class DoxygenCompoundParser {
    private final ComponentsRegistry componentsRegistry;
    private final String content;
    private final String id;
    private final DoxygenCompound compound;

    private DoxygenCompoundParser(ComponentsRegistry componentsRegistry, String content, String id) {
        this.componentsRegistry = componentsRegistry;
        this.content = content;
        this.id = id;
        this.compound = new DoxygenCompound();
    }

    public static DoxygenCompound parse(ComponentsRegistry componentsRegistry, String content, String id) {
        DoxygenCompoundParser parser = new DoxygenCompoundParser(componentsRegistry, content, id);
        parser.parseXml();

        return parser.compound;
    }

    private void parseXml() {
        Document document = XmlUtils.parseXml(content);
        Node root = XmlUtils.anyNestedNodeByName(document, "doxygen");
        Node compoundRoot = XmlUtils.allNestedNodesStreamByName(root, "compounddef")
                .filter(node -> id.equals(XmlUtils.getAttributeText(node, "id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find compound with id: " + id));

        compound.id = XmlUtils.getAttributeText(compoundRoot, "id");
        compound.kind = XmlUtils.getAttributeText(compoundRoot, "kind");
        compound.name = XmlUtils.nextLevelNodeByName(compoundRoot, "compoundname").getTextContent();

        Node description = XmlUtils.nextLevelNodeByName(compoundRoot, "detaileddescription");
        compound.description = DoxygenDescriptionParser.parse(componentsRegistry, id, description);

        XmlUtils.allNestedNodesStreamByName(compoundRoot, "memberdef").forEach(this::parseMember);
    }

    private void parseMember(Node memberNode) {
        DoxygenMember doxygenMember = DoxygenMemberParser.parse(componentsRegistry, memberNode);
        doxygenMember.setCompound(compound);
        compound.addMember(doxygenMember);
    }
}
