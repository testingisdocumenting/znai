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
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Node;

import java.util.List;
import java.util.stream.Collectors;

public class DoxygenDescriptionParamsParser {
    private final ComponentsRegistry componentsRegistry;
    private final ApiParameters apiParameters;
    private final DoxygenParameterList parameters;
    private final String anchorPrefix;
    private final Node node;

    private DoxygenDescriptionParamsParser(ComponentsRegistry componentsRegistry,
                                           DoxygenParameterList parameters,
                                           String anchorPrefix, Node node) {
        this.componentsRegistry = componentsRegistry;
        this.parameters = parameters;
        this.anchorPrefix = anchorPrefix;
        this.node = node;
        this.apiParameters = new ApiParameters(anchorPrefix);
    }

    /**
     * parse parameters description from doxygen description parameters block
     * @param componentsRegistry components registry to help with creation of parser handlers and other things
     * @param parameters optional list of parameters extracted from a member to grab types as doxygen desc doesn't have types, only names
     * @param anchorPrefix anchor for created api parameters
     * @param node node to parse
     * @return parsed parameters
     */
    public static ApiParameters parseParameters(ComponentsRegistry componentsRegistry,
                                                DoxygenParameterList parameters,
                                                String anchorPrefix, Node node) {
        DoxygenDescriptionParamsParser parser = new DoxygenDescriptionParamsParser(componentsRegistry,
                parameters, anchorPrefix, node);

        parser.parseXml();

        return parser.apiParameters;
    }

    private void parseXml() {
        XmlUtils.allNestedNodesStreamByName(node, "parameteritem").forEach(this::handleParam);
    }

    private void handleParam(Node paramItem) {
        Node nameAndType = XmlUtils.nextLevelNodeByName(paramItem, "parameternamelist");
        String name = XmlUtils.nextLevelNodeByName(nameAndType, "parametername").getTextContent();
        String typeText = XmlUtils.hasNodeByName(nameAndType, "parametertype") ?
                XmlUtils.nextLevelNodeByName(nameAndType, "parametertype").getTextContent() :
                "";

        ApiLinkedText type = linkedTextByName(name, typeText);

        Node descriptionNode = XmlUtils.nextLevelNodeByName(paramItem, "parameterdescription");
        DoxygenDescription paramDescription = DoxygenDescriptionParser.parse(componentsRegistry,
                parameters,
                anchorPrefix + "_",
                descriptionNode);

        apiParameters.add(name, type,
                paramDescription.getDocElements().stream().map(DocElement::toMap).collect(Collectors.toList()),
                paramDescription.getSearchTextWithoutParameters());
    }

    private ApiLinkedText linkedTextByName(String name, String existingTypeText) {
        if (!existingTypeText.isEmpty()) {
            return new ApiLinkedText(existingTypeText);
        }

        DoxygenParameter byName = parameters.findByName(name);
        if (byName != null) {
            return byName.getType();
        }

        return new ApiLinkedText();
    }
}
