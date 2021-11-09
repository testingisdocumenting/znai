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
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Node;

import java.util.stream.Collectors;

public class DoxygenDescriptionParamsParser {
    private final ApiParameters apiParameters;
    private final ComponentsRegistry componentsRegistry;
    private final String anchorPrefix;
    private final Node node;

    private DoxygenDescriptionParamsParser(ComponentsRegistry componentsRegistry, String anchorPrefix, Node node) {
        this.componentsRegistry = componentsRegistry;
        this.anchorPrefix = anchorPrefix;
        this.node = node;
        this.apiParameters = new ApiParameters(anchorPrefix);
    }

    public static ApiParameters parseParameters(ComponentsRegistry componentsRegistry, String anchorPrefix, Node node) {
        DoxygenDescriptionParamsParser parser = new DoxygenDescriptionParamsParser(componentsRegistry,
                anchorPrefix, node);

        parser.parseXml();

        return parser.apiParameters;
    }

    private void parseXml() {
        XmlUtils.nodesStreamByName(node, "parameteritem").forEach(this::handleParam);
    }

    private void handleParam(Node paramItem) {
        Node nameAndType = XmlUtils.nodeByName(paramItem, "parameternamelist");
        String name = XmlUtils.nodeByName(nameAndType, "parametername").getTextContent();
        String type = XmlUtils.hasNodeByName(nameAndType, "parametertype") ?
                XmlUtils.nodeByName(nameAndType, "parametertype").getTextContent() :
                "";

        Node descriptionNode = XmlUtils.nodeByName(paramItem, "parameterdescription");
        DoxygenDescription paramDescription = DoxygenDescriptionParser.parse(componentsRegistry,
                anchorPrefix + "_",
                descriptionNode);
        apiParameters.add(name, type,
                paramDescription.getDocElements().stream().map(DocElement::toMap).collect(Collectors.toList()),
                paramDescription.getSearchTextWithoutParameters());
    }
}
