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
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.w3c.dom.Node;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DoxygenDescriptionParser {
    private final ComponentsRegistry componentsRegistry;
    private final DoxygenParameterList parameters;
    private final String paramsAnchorPrefix;
    private final Node descriptionRoot;
    private final List<String> textParts;

    private DocElementCreationParserHandler parserHandler;
    private ApiParameters apiParameters;
    private ApiParameters apiTemplateParameters;

    private DoxygenDescriptionParser(ComponentsRegistry componentsRegistry,
                                     DoxygenParameterList parameters,
                                     String paramsAnchorPrefix, Node descriptionNode) {
        this.componentsRegistry = componentsRegistry;
        this.parameters = parameters;
        this.paramsAnchorPrefix = paramsAnchorPrefix;
        this.descriptionRoot = descriptionNode;
        this.textParts = new ArrayList<>();
    }

    /**
     * parse parameters description from doxygen description block
     * @param componentsRegistry components registry to help with creation of parser handlers and other things
     * @param parameters optional list of parameters extracted from a member to grab types as doxygen desc doesn't have types, only names
     * @param paramsAnchorPrefix anchor for created api parameters
     * @param descriptionNode node to parse
     * @return parsed description
     */
    public static DoxygenDescription parse(ComponentsRegistry componentsRegistry,
                                           DoxygenParameterList parameters,
                                           String paramsAnchorPrefix,
                                           Node descriptionNode) {
        DoxygenDescriptionParser parser = new DoxygenDescriptionParser(componentsRegistry,
                parameters,
                paramsAnchorPrefix,
                descriptionNode);

        return parser.parseXml();
    }

    private DoxygenDescription parseXml() {
        parserHandler = new DocElementCreationParserHandler(componentsRegistry, Paths.get("doxygen-xml"));

        parseChildren(descriptionRoot);
        parserHandler.onParsingEnd();

        return new DoxygenDescription(parserHandler.getDocElement().getContent(),
                apiParameters,
                apiTemplateParameters,
                String.join(" ", textParts));
    }

    private void parseChildren(Node node) {
        XmlUtils.forEach(node.getChildNodes(), this::handleDescriptionNode);
    }

    private void handleDescriptionNode(Node node) {
        String nodeName = node.getNodeName();
        if (nodeName.equals("para")) {
            parserHandler.onParagraphStart();
            parseChildren(node);
            parserHandler.onParagraphEnd();
        } else if (nodeName.equals("parameterlist")) {
            String kind = XmlUtils.getAttributeText(node, "kind");
            if ("param".equals(kind)) {
                apiParameters = DoxygenDescriptionParamsParser.parseParameters(componentsRegistry, parameters, paramsAnchorPrefix, node);
            } else if ("templateparam".equals(kind)) {
                apiTemplateParameters = DoxygenDescriptionParamsParser.parseParameters(componentsRegistry, parameters, paramsAnchorPrefix + "_template", node);
            }
            parseChildren(node);
        } else if (nodeName.equals("programlisting")) {
            DoxygenCodeBlockSimple codeBlockSimple = DoxygenProgramListingParser.parseAsSimpleCodeBlock(node);
            parserHandler.onSnippet(PluginParams.EMPTY, codeBlockSimple.getExtension(), "", codeBlockSimple.getCode());
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
        } else if (nodeName.equals("itemizedlist")) {
            parserHandler.onBulletListStart('*', false);
            parseChildren(node);
            parserHandler.onBulletListEnd();
        } else if (nodeName.equals("orderedlist")) {
            parserHandler.onOrderedListStart(' ', 1);
            parseChildren(node);
            parserHandler.onOrderedListEnd();
        } else if (nodeName.equals("listitem")) {
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
