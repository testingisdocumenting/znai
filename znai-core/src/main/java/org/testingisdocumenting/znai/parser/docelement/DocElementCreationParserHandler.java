/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.parser.docelement;

import org.testingisdocumenting.znai.codesnippets.CodeSnippetsProps;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.file.SnippetContentProvider;
import org.testingisdocumenting.znai.extensions.file.SnippetHighlightFeature;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.extensions.Plugin;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.Plugins;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.PageSectionIdTitle;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DocElementCreationParserHandler implements ParserHandler {
    private final ComponentsRegistry componentsRegistry;
    private final Path path;
    private final List<AuxiliaryFile> auxiliaryFiles;

    private final List<String> globalAnchorIds;

    private final List<DocElement> paragraphs;

    private final DocElement docElement;
    private final Deque<DocElement> elementsStack;

    private String currentSectionTitle;
    private SubHeadingUniqueIdGenerator subHeadingUniqueIdGenerator;

    private boolean isSectionStarted;

    public DocElementCreationParserHandler(ComponentsRegistry componentsRegistry, Path path) {
        this.componentsRegistry = componentsRegistry;
        this.path = path;
        this.paragraphs = new ArrayList<>();
        this.auxiliaryFiles = new ArrayList<>();

        this.globalAnchorIds = new ArrayList<>();

        this.docElement = new DocElement(DocElementType.PAGE);
        this.elementsStack = new ArrayDeque<>();
        this.elementsStack.add(docElement);

        this.currentSectionTitle = "";
        this.isSectionStarted = false;
        this.subHeadingUniqueIdGenerator = new SubHeadingUniqueIdGenerator("");
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public List<AuxiliaryFile> getAuxiliaryFiles() {
        return auxiliaryFiles;
    }

    public List<String> getGlobalAnchorIds() {
        return globalAnchorIds;
    }

    @Override
    public void onSectionStart(String title) {
        currentSectionTitle = title;

        if (isSectionStarted) {
            onSectionEnd();
        }

        String id = new PageSectionIdTitle(title).getId();
        start(DocElementType.SECTION,
                "title", title,
                "id", id);
        subHeadingUniqueIdGenerator = new SubHeadingUniqueIdGenerator(id);

        componentsRegistry.docStructure().registerLocalAnchor(path, id);

        isSectionStarted = true;
    }

    @Override
    public void onSectionEnd() {
        currentSectionTitle = "";
        isSectionStarted = false;

        // on section end is called for plugin processing (if a plugin returns a new section)
        // it is also being called every time a new section starts -- to close the previous one
        // two events can collide and section will be closed twice
        // see: MarkdownVisitor#visit(Heading) and processPlugin method
        if (elementsStack.size() > 1) {
            end();
        }
    }

    @Override
    public void onSubHeading(int level, String title) {
        String idByTitle = new PageSectionIdTitle(title).getId();

        subHeadingUniqueIdGenerator.registerSubHeading(level, idByTitle);
        String id = subHeadingUniqueIdGenerator.generateId();

        append(DocElementType.SUB_HEADING,
                "level", level,
                "title", title,
                "id", id);

        componentsRegistry.docStructure().registerLocalAnchor(path, id);
    }

    @Override
    public void onHardLineBreak() {
        append(DocElementType.HARD_LINE_BREAK);
    }

    @Override
    public void onSoftLineBreak() {
        append(DocElementType.SOFT_LINE_BREAK);
    }

    @Override
    public void onParagraphStart() {
        start(DocElementType.PARAGRAPH);
    }

    @Override
    public void onParagraphEnd() {
        DocElement paragraph = end();
        paragraphs.add(paragraph);
    }

    @Override
    public void onBulletListStart(char bulletMarker, boolean tight) {
        start(DocElementType.BULLET_LIST, "bulletMarker", bulletMarker, "tight", tight);
    }

    @Override
    public void onBulletListEnd() {
        end();
    }

    @Override
    public void onOrderedListStart(char delimiter, int startNumber) {
        start(DocElementType.ORDERED_LIST, "delimiter", delimiter, "startNumber", startNumber);
    }

    @Override
    public void onOrderedListEnd() {
        end();
    }

    @Override
    public void onListItemStart() {
        start(DocElementType.LIST_ITEM);
    }

    @Override
    public void onListItemEnd() {
        end();
    }

    @Override
    public void onTable(MarkupTableData tableData) {
        append(new DocElement(DocElementType.TABLE, "table", tableData.toMap()));
    }

    @Override
    public void onEmphasisStart() {
        start(DocElementType.EMPHASIS);
    }

    @Override
    public void onEmphasisEnd() {
        end();
    }

    @Override
    public void onStrongEmphasisStart() {
        start(DocElementType.STRONG_EMPHASIS);
    }

    @Override
    public void onStrongEmphasisEnd() {
        end();
    }

    @Override
    public void onStrikeThroughStart() {
        start(DocElementType.STRIKE_THROUGH);
    }

    @Override
    public void onStrikeThroughEnd() {
        end();
    }

    @Override
    public void onBlockQuoteStart() {
        start(DocElementType.BLOCK_QUOTE);
    }

    @Override
    public void onBlockQuoteEnd() {
        end();
    }

    @Override
    public void onSimpleText(String value) {
        append(DocElementType.SIMPLE_TEXT, "text", value);
    }

    @Override
    public void onInlinedCode(String inlinedCode, DocReferences docReferences) {
        Map<String, Object> props = new HashMap<>();
        props.put("code", inlinedCode);
        if (!docReferences.isEmpty()) {
            props.put("references", docReferences.toMap());
        }

        append(DocElementType.INLINED_CODE, props);
    }

    @Override
    public void onLinkStart(String url) {
        boolean isFile = isLocalFile(url);
        String convertedUrl = isFile ?
                convertAndRegisterLocalFileToUrl(url):
                validateAndCovertUrl(url);

        start(DocElementType.LINK, "url", convertedUrl, "isFile", isFile);
    }

    @Override
    public void onLinkEnd() {
        end();
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        DocStructure docStructure = componentsRegistry.docStructure();
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        AuxiliaryFile auxiliaryFile = resourcesResolver.runtimeAuxiliaryFile(destination);

        BufferedImage image = resourcesResolver.imageContent(destination);

        append(DocElementType.IMAGE, "title", title,
                "destination", docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString()),
                "alt", alt,
                "inlined", true,
                "timestamp", componentsRegistry.timeService().fileModifiedTimeMillis(auxiliaryFile.getPath()),
                "width", image.getWidth(),
                "height", image.getHeight());

        if (!destination.startsWith("http")) {
            auxiliaryFiles.add(auxiliaryFile);
        }
    }

    @Override
    public void onSnippet(PluginParams pluginParams, String lang, String lineNumber, String snippet) {
        Map<String, Object> snippetProps = CodeSnippetsProps.create(lang, snippet);
        snippetProps.put("lineNumber", lineNumber);
        snippetProps.putAll(pluginParams.getOpts().toMap());

        SnippetHighlightFeature highlightFeature = new SnippetHighlightFeature(componentsRegistry, pluginParams,
                new SnippetContentProvider() {
                    @Override
                    public String snippetContent() {
                        return snippet;
                    }

                    @Override
                    public String snippetId() {
                        return "embedded-snippet";
                    }
                });
        highlightFeature.updateProps(snippetProps);

        append(DocElementType.SNIPPET, snippetProps);
    }

    @Override
    public void onThematicBreak() {
        append(DocElementType.THEMATIC_BREAK);
    }

    @Override
    public void onCustomNodeStart(String nodeName, Map<String, ?> attrs) {
        DocElement docElement = new DocElement(nodeName);
        attrs.forEach(docElement::addProp);

        appendAndPush(docElement);
    }

    @Override
    public void onCustomNode(String nodeName, Map<String, ?> attrs) {
        append(nodeName, attrs);
    }

    @Override
    public void onCustomNodeEnd(String nodeName) {
        end();
    }

    @Override
    public void onGlobalAnchor(String id) {
        componentsRegistry.docStructure().registerGlobalAnchor(path, id);
        append("Anchor", "id", id);
    }

    @Override
    public void onGlobalAnchorRefStart(String id) {
        String anchorUrl = componentsRegistry.docStructure().globalAnchorUrl(path, id);
        onLinkStart(anchorUrl);
    }

    @Override
    public void onGlobalAnchorRefEnd() {
        onLinkEnd();
    }

    @Override
    public void onIncludePlugin(IncludePlugin includePlugin, PluginResult pluginResult) {
        processPlugin(includePlugin, pluginResult);
    }

    @Override
    public void onFencePlugin(FencePlugin fencePlugin, PluginResult pluginResult) {
        processPlugin(fencePlugin, pluginResult);
    }

    @Override
    public void onInlinedCodePlugin(InlinedCodePlugin inlinedCodePlugin, PluginResult pluginResult) {
        processPlugin(inlinedCodePlugin, pluginResult);
    }

    private <E extends Plugin> void processPlugin(E plugin, PluginResult result) {
        plugin.auxiliaryFiles(componentsRegistry).forEach(auxiliaryFiles::add);

        List<DocElement> docElements = result.getDocElements();
        if (docElements.isEmpty()) {
            return;
        }

        docElements.forEach(el -> {
            // if element is a section itself we need to close all the current sections and paragraphs
            if (el.getType().equals(DocElementType.SECTION)) {
                while (elementsStack.size() > 1) {
                    end();
                }
            }

            append(el);
        });
    }

    @Override
    public void onParsingEnd() {
        removeEmptyParagraphs(docElement);
        paragraphs.forEach(this::convertParagraphWithSingleImageToWideImage);
    }

    /**
     * we have empty paragraphs if the include-plugin is used outside of any sections.
     * it leaves an empty paragraph
     * @param element element to recursively remove paragraphs
     */
    private void removeEmptyParagraphs(DocElement element) {
        List<DocElement> emptyParagraphs = element.getContent().stream()
                .filter(e -> e.getType().equals(DocElementType.PARAGRAPH) && e.getContent().isEmpty())
                .collect(Collectors.toList());

        emptyParagraphs.forEach(element::removeChild);
        element.getContent().forEach(this::removeEmptyParagraphs);
    }

    private void convertParagraphWithSingleImageToWideImage(DocElement paragraph) {
        if (paragraph.getContent().size() != 1) {
            return;
        }

        DocElement singleElement = paragraph.getContent().get(0);
        if (! singleElement.getType().equals(DocElementType.IMAGE)) {
            return;
        }

        paragraph.setType(singleElement.getType());

        singleElement.getProps().forEach(paragraph::addProp);
        paragraph.addProp("inlined", false);

        paragraph.removeChild(singleElement);
    }

    private void start(String type, Object... propsKeyValue) {
        appendAndPush(new DocElement(type, propsKeyValue));
    }

    private DocElement end() {
        return elementsStack.removeLast();
    }

    private void append(String type, Object... propsKeyValue) {
        append(new DocElement(type, propsKeyValue));
    }

    private void append(String type, Map<String, ?> propsKeyValue) {
        DocElement element = new DocElement(type);
        propsKeyValue.forEach(element::addProp);

        append(element);
    }

    private void append(DocElement element) {
        elementsStack.peekLast().addChild(element);
    }

    private void appendAndPush(DocElement element) {
        elementsStack.peekLast().addChild(element);
        elementsStack.add(element);
    }

    private String validateAndCovertUrl(String url) {
        DocStructure docStructure = componentsRegistry.docStructure();
        DocUrl docUrl = new DocUrl(url);

        docStructure.validateUrl(path, "section title: " + currentSectionTitle, docUrl);
        return docStructure.createUrl(path, docUrl);
    }

    private boolean isLocalFile(String url) {
        if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("mailto:")) {
            return false;
        }

        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        return url.indexOf('.') != -1 && resourcesResolver.isLocalFile(url);
    }

    private String convertAndRegisterLocalFileToUrl(String url) {
        DocStructure docStructure = componentsRegistry.docStructure();
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();

        AuxiliaryFile auxiliaryFile = resourcesResolver.runtimeAuxiliaryFile(url);
        auxiliaryFiles.add(auxiliaryFile);

        return docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString());
    }
}

