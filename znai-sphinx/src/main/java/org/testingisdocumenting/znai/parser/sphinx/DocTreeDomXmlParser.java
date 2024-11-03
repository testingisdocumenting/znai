/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.parser.sphinx;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.sphinx.python.*;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.testingisdocumenting.znai.parser.sphinx.xml.DocUtilsXmlFixer.fixDocUtilsIncorrectXml;

class DocTreeDomXmlParser {
    private final ComponentsRegistry componentsRegistry;
    private final Path filePath;
    private final ParserHandler parserHandler;

    DocTreeDomXmlParser(ComponentsRegistry componentsRegistry,
                        Path filePath,
                        ParserHandler parserHandler) {
        this.componentsRegistry = componentsRegistry;
        this.filePath = filePath;
        this.parserHandler = parserHandler;
    }

    public void parse(String xml) {
        xml = fixDocUtilsIncorrectXml(xml);
        Document document = XmlUtils.parseXml(xml);
        NodeList nodes = document.getChildNodes();

        parseNodeList(nodes);
    }

    private boolean parseNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            parseNode(item);
        }

        return true;
    }

    private boolean parseNode(Node node) {
        switch (node.getNodeName()) {
            case "document":
                return parseChildren(node);
            case "section":
                return parseSection(node);
            case "paragraph":
                return parseParagraph(node);
            case "emphasis":
                return parseEmphasis(node);
            case "strong":
                return parseStrong(node);
            case "literal":
                return parseLiteral(node);
            case "literal_strong":
                return parseLiteralStrong(node);
            case "literal_emphasis":
                return parseLiteralEmphasis(node);
            case "literal_block":
                return parseSnippet(node);
            case "bullet_list":
                return parseBulletList(node);
            case "enumerated_list":
                return parseOrderedList(node);
            case "list_item":
                return parseListItem(node);
            case "reference":
                return parseReference(node);
            case "#text":
                return parseText(node);
            case "title":
                return false;
            case "desc_signature":
                return parseDescSignature(node);
            case "field_list":
            case "field":
            case "field_name":
            case "field_body":
            case "desc":
            case "desc_parameterlist":
            case "desc_name":
            case "desc_addname":
            case "desc_annotation":
            case "desc_content":
            case "desc_parameter":
            case "desc_optional":
                return parseDocUtils(node);
        }

        return false;
    }

    private boolean parseDescSignature(Node node) {
        if (XmlUtils.hasAttributeText(node, "ids")) {
            String ids = XmlUtils.getAttributeText(node, "ids");
            parserHandler.onGlobalAnchor(ids);
        }

        return parseDocUtils(node);
    }

    private boolean parseDocUtils(Node node) {
        String nodeName = convertNodeName(node.getNodeName());

        parserHandler.onCustomNodeStart(nodeName, camelCaseKeys(XmlUtils.getAttributes(node)));
        parseChildren(node);
        parserHandler.onCustomNodeEnd(nodeName);

        return true;
    }

    private boolean parseReference(Node node) {
        parserHandler.onLinkStart(filePath, XmlUtils.getAttributeText(node, "refuri"));
        parseChildren(node);
        parserHandler.onLinkEnd();

        return true;
    }

    private boolean parseSection(Node node) {
        parserHandler.onSectionStart(extractTitle(node), HeadingProps.EMPTY, null);
        parseChildren(node);
        parserHandler.onSectionEnd();

        return true;
    }

    private boolean parseParagraph(Node node) {
        parserHandler.onParagraphStart();
        parseChildren(node);
        parserHandler.onParagraphEnd();

        return true;
    }

    private boolean parseEmphasis(Node node) {
        parserHandler.onEmphasisStart();
        parseChildren(node);
        parserHandler.onEmphasisEnd();

        return true;
    }

    private boolean parseStrong(Node node) {
        parserHandler.onStrongEmphasisStart();
        parseChildren(node);
        parserHandler.onStrongEmphasisEnd();

        return true;
    }

    private boolean parseLiteral(Node node) {
        parserHandler.onInlinedCode(node.getTextContent(), DocReferences.EMPTY);
        return true;
    }

    private boolean parseLiteralStrong(Node node) {
        parserHandler.onStrongEmphasisStart();
        parserHandler.onInlinedCode(node.getTextContent(), DocReferences.EMPTY);
        parserHandler.onStrongEmphasisEnd();
        return true;
    }

    private boolean parseLiteralEmphasis(Node node) {
        parserHandler.onEmphasisStart();
        parserHandler.onInlinedCode(node.getTextContent(), DocReferences.EMPTY);
        parserHandler.onEmphasisEnd();
        return true;
    }

    private boolean parseSnippet(Node node) {
        parserHandler.onSnippet(PluginParams.EMPTY, XmlUtils.getAttributeText(node, "language"),
                "", node.getTextContent());

        return true;
    }

    private boolean parseBulletList(Node node) {
        parserHandler.onBulletListStart(XmlUtils.getAttributeText(node, "bullet", "*").charAt(0), false);
        parseChildren(node);
        parserHandler.onBulletListEnd();

        return true;
    }

    private boolean parseOrderedList(Node node) {
        parserHandler.onOrderedListStart(XmlUtils.getAttributeText(node, "suffix").charAt(0), 1);
        parseChildren(node);
        parserHandler.onOrderedListEnd();

        return true;
    }

    private boolean parseListItem(Node node) {
        parserHandler.onListItemStart();
        parseChildren(node);
        parserHandler.onListItemEnd();

        return true;
    }

    private boolean parseDesc(Node node) {
        String descType = XmlUtils.getAttributeText(node, "desctype");

        switch (descType) {
            case "class":
                return parseClass(node);
            case "function":
                return parseFunction(node);
            default:
                return false;
        }
    }

    private boolean parseFunction(Node node) {
        PythonFunction pythonFunction = new PythonFunctionXmlParser().parse(node);
        parserHandler.onGlobalAnchor(pythonFunction.getRefId());

        PythonFunctionIncludePlugin includePlugin = new PythonFunctionIncludePlugin();

        PluginResult pluginResult = includePlugin.process(componentsRegistry, parserHandler, filePath,
                componentsRegistry.pluginParamsFactory().create(includePlugin.id(), "", pythonFunction.toMap()));

        parserHandler.onIncludePlugin(includePlugin, pluginResult);

        return false;
    }

    private boolean parseClass(Node node) {
        PythonClassXmlParser xmlParser = new PythonClassXmlParser();
        PythonClass pythonClass = xmlParser.parseClass(node);

        parserHandler.onGlobalAnchor(pythonClass.getRefId());
        pythonClass.getMethods().forEach(m -> parserHandler.onGlobalAnchor(m.getRefId()));

        PythonClassIncludePlugin includePlugin = new PythonClassIncludePlugin();
        PluginResult pluginResult = includePlugin.process(componentsRegistry, parserHandler, filePath,
                componentsRegistry.pluginParamsFactory().create(includePlugin.id(), "", pythonClass.toMap()));

        parserHandler.onIncludePlugin(includePlugin, pluginResult);

        return true;
    }

    private boolean parseText(Node node) {
        String textContent = node.getTextContent();
        if (textContent.startsWith("\n")) {
            return false;
        }

        parserHandler.onSimpleText(textContent);
        return true;
    }

    private boolean parseChildren(Node node) {
        return parseNodeList(node.getChildNodes());
    }

    private String extractTitle(Node node) {
        Element element = (Element) node;
        return element.getElementsByTagName("title").item(0).getTextContent();
    }

    private static Map<String, String> camelCaseKeys(Map<String, String> attributes) {
        Map<String, String> result = new LinkedHashMap<>();
        attributes.forEach((k, v) -> result.put(convertAttributeName(k), v));

        return result;
    }

    private static String convertNodeName(String name) {
        return "DocUtils" + WordUtils.capitalizeFully(name, '_')
                .replaceAll("_", "");
    }

    private static String convertAttributeName(String name) {
        name = name.replace(":", "_");
        return StringUtils.uncapitalize(WordUtils.capitalizeFully(name, '_')
                .replaceAll("_", ""));
    }
}
