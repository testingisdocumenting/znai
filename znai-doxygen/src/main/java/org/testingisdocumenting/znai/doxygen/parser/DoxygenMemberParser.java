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
import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Node;

public class DoxygenMemberParser {
    private final ComponentsRegistry componentsRegistry;
    private final Node memberNode;
    private final DoxygenMember member;

    private DoxygenMemberParser(ComponentsRegistry componentsRegistry, Node memberNode) {
        this.componentsRegistry = componentsRegistry;
        this.memberNode = memberNode;
        this.member = new DoxygenMember();
    }

    public static DoxygenMember parse(ComponentsRegistry componentsRegistry, Node memberNode) {
        DoxygenMemberParser parser = new DoxygenMemberParser(componentsRegistry, memberNode);
        parser.parseXml();

        return parser.member;
    }

    private void parseXml() {
        parseMember(memberNode);
    }

    private void parseMember(Node memberNode) {
        member.setId(XmlUtils.getAttributeText(memberNode, "id"));
        member.setName(XmlUtils.nextLevelNodeByName(memberNode, "name").getTextContent());
        member.setKind(XmlUtils.getAttributeText(memberNode, "kind"));
        member.setVisibility(XmlUtils.getAttributeText(memberNode, "prot"));
        member.setStatic("yes".equals(XmlUtils.getAttributeText(memberNode, "static")));
        member.setVirtual("virtual".equals(XmlUtils.getAttributeText(memberNode, "virt")));
        member.setConst("yes".equals(XmlUtils.getAttributeText(memberNode, "const")));

        member.setReturnType(DoxygenTextWithLinksParser.parse(XmlUtils.nextLevelNodeByName(memberNode, "type")));

        XmlUtils.childrenNodesStreamByName(memberNode, "param").forEach((paramNode) -> {
            String name = XmlUtils.nextLevelNodeByName(paramNode, "declname").getTextContent();
            ApiLinkedText type = DoxygenTextWithLinksParser.parse(XmlUtils.nextLevelNodeByName(paramNode, "type"));
            member.addParameter(name, type);
        });

        member.setDescription(DoxygenDescriptionParser.parse(componentsRegistry,
                member.getParameters(),
                member.getName(),
                XmlUtils.anyNestedNodeByName(memberNode, "detaileddescription")));
    }
}
