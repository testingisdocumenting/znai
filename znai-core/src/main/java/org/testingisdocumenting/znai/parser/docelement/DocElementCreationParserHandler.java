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
import org.testingisdocumenting.znai.extensions.Plugin;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.fence.FencePlugin;
import org.testingisdocumenting.znai.extensions.file.AnchorFeature;
import org.testingisdocumenting.znai.extensions.file.SnippetContentProvider;
import org.testingisdocumenting.znai.extensions.file.SnippetHighlightFeature;
import org.testingisdocumenting.znai.extensions.footnote.FootnoteId;
import org.testingisdocumenting.znai.extensions.footnote.ParsedFootnote;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.PageSectionIdTitle;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.table.MarkupTableData;
import org.testingisdocumenting.znai.reference.DocReferences;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.structure.AnchorIds;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.structure.DocUrl;
import org.testingisdocumenting.znai.structure.TocItem;
import org.testingisdocumenting.znai.utils.UrlUtils;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

public class DocElementCreationParserHandler implements ParserHandler {
    private final ComponentsRegistry componentsRegistry;
    private final Path path;
    private final List<AuxiliaryFile> auxiliaryFiles;

    private final List<String> globalAnchorIds;

    private final List<DocElement> paragraphs;

    private final DocElement docElement;
    private final Deque<DocElement> elementsStack;

    private final Map<FootnoteId, ParsedFootnote> parsedFootnotes;
    private String currentSectionTitle;

    private boolean isSectionStarted;

    public DocElementCreationParserHandler(ComponentsRegistry componentsRegistry, Path path) {
        this.componentsRegistry = componentsRegistry;
        this.path = path;
        this.paragraphs = new ArrayList<>();
        this.auxiliaryFiles = new ArrayList<>();

        this.globalAnchorIds = new ArrayList<>();

        this.parsedFootnotes = new HashMap<>();

        this.docElement = new DocElement(DocElementType.PAGE);
        this.elementsStack = new ArrayDeque<>();
        this.elementsStack.add(docElement);

        this.currentSectionTitle = "";
        this.isSectionStarted = false;
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

    public String getCurrentSectionTitle() {
        return currentSectionTitle;
    }

    @Override
    public void onSectionStart(String title, HeadingProps headingProps) {
        currentSectionTitle = title;

        if (isSectionStarted) {
            onSectionEnd();
        }

        Map<String, ?> headingPropsMap = headingProps.props();

        DocStructure docStructure = componentsRegistry.docStructure();
        String sectionId = new PageSectionIdTitle(title, headingPropsMap).getId();
        docStructure.onSectionOrSubHeading(path, 1, sectionId);

        var anchorIds = docStructure.generateUniqueAnchors(path, "");
        Map<String, Object> props = new LinkedHashMap<>(headingPropsMap);
        addAnchorIdsToProps(props, anchorIds);
        props.put("title", title);

        start(DocElementType.SECTION, props);
        docStructure.registerLocalAnchors(path, anchorIds);

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
    public void onSubHeading(int level, String title, HeadingProps headingProps) {
        Map<String, ?> headingPropsMap = headingProps.props();

        String idByTitle = new PageSectionIdTitle(title, headingPropsMap).getId();

        DocStructure docStructure = componentsRegistry.docStructure();
        docStructure.onSectionOrSubHeading(path, level, idByTitle);

        var ids = headingProps.isCustomAnchorIdSet() ?
                new AnchorIds(headingProps.getAnchorId(), Collections.emptyList()):
                docStructure.generateUniqueAnchors(path, "");
        docStructure.registerLocalAnchors(path, ids);

        Map<String, Object> props = new LinkedHashMap<>(headingPropsMap);
        addAnchorIdsToProps(props, ids);
        props.put("level", level);
        props.put("title", title);
        append(DocElementType.SUB_HEADING, props);
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
    public void onFootnoteDefinition(ParsedFootnote footnote) {
        parsedFootnotes.put(footnote.id(), footnote);
    }

    @Override
    public void onFootnoteReference(FootnoteId footnoteId) {
        append(new DocElement( "FootnoteReference",
                "label", footnoteId.id(),
                "content", (Supplier<?>) (() -> footnoteContent(footnoteId))));
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
    public void onLinkStart(Path markupPath, String url) {
        DocUrl docUrl = new DocUrl(markupPath, url);

        boolean isFile = isLocalFile(docUrl, url);
        String convertedUrl = isFile ?
                convertAndRegisterLocalFileToUrl(url):
                validateAndCovertUrl(docUrl);

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
        boolean isExternal = UrlUtils.isExternal(destination);

        if (!isExternal) {
            AuxiliaryFile auxiliaryFile = resourcesResolver.runtimeAuxiliaryFile(destination);
            BufferedImage image = resourcesResolver.imageContent(destination);

            Map<String, Object> props = new HashMap<>();
            props.put("title", title);
            props.put("destination", docStructure.fullUrl(auxiliaryFile.getDeployRelativePath().toString()));
            props.put("alt", alt);
            props.put("inlined", true);
            props.put("timestamp", componentsRegistry.timeService().fileModifiedTimeMillis(auxiliaryFile.getPath()));

            if (image != null) {
                props.put("width", image.getWidth());
                props.put("height", image.getHeight());
            }

            append(DocElementType.IMAGE, props);

            auxiliaryFiles.add(auxiliaryFile);
        } else {
            docStructure.validateUrl(path, "![]() image", new DocUrl(path, destination));
            append(DocElementType.IMAGE, "title", title,
                    "destination", destination,
                    "alt", alt,
                    "inlined", true);
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

        AnchorFeature anchorFeature = new AnchorFeature(componentsRegistry.docStructure(), path, pluginParams);
        anchorFeature.updateProps(snippetProps);

        append(DocElementType.SNIPPET, snippetProps);
    }

    @Override
    public void onThematicBreak() {
        append(DocElementType.THEMATIC_BREAK);
    }

    @Override
    public void onHtml(String html, boolean isInlined) {
        append("EmbeddedHtml", "html", html, "isInlined", isInlined);
    }

    @Override
    public void onCustomNodeStart(String nodeName, Map<String, ?> attrs) {
        DocElement docElement = new DocElement(nodeName);
        docElement.addProps(attrs);

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
    public void onDocElement(DocElement docElement) {
        append(docElement);
    }

    @Override
    public void onGlobalAnchor(String id) {
        componentsRegistry.docStructure().registerGlobalAnchor(path, id);
        append("Anchor", "id", id);
    }

    @Override
    public void onGlobalAnchorRefStart(String id) {
        String anchorUrl = componentsRegistry.docStructure().globalAnchorUrl(path, id);
        onLinkStart(path, anchorUrl);
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
                .toList();

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

    private void start(String type, Map<String, ?> propsKeyValue) {
        DocElement element = new DocElement(type);
        element.addProps(propsKeyValue);

        appendAndPush(element);
    }

    private DocElement end() {
        return elementsStack.removeLast();
    }

    private void append(String type, Object... propsKeyValue) {
        append(new DocElement(type, propsKeyValue));
    }

    private void append(String type, Map<String, ?> propsKeyValue) {
        DocElement element = new DocElement(type);
        element.addProps(propsKeyValue);

        append(element);
    }

    private void append(DocElement element) {
        elementsStack.peekLast().addChild(element);
    }

    private void appendAndPush(DocElement element) {
        elementsStack.peekLast().addChild(element);
        elementsStack.add(element);
    }

    private String validateAndCovertUrl(DocUrl docUrl) {
        DocStructure docStructure = componentsRegistry.docStructure();

        docStructure.validateUrl(path, "section title: " + currentSectionTitle, docUrl);
        return docStructure.createUrl(path, docUrl);
    }

    private boolean isLocalFile(DocUrl docUrl, String url) {
        if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("mailto:")) {
            return false;
        }

        TocItem tocItem = componentsRegistry.docStructure().tableOfContents().findTocItem(docUrl.getDirName(), docUrl.getFileNameWithoutExtension());
        if (tocItem != null) {
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

    private void addAnchorIdsToProps(Map<String, Object> props, AnchorIds ids) {
        props.put("id", ids.main());
        props.put("additionalIds", ids.additional());
    }

    private List<Map<String, Object>> footnoteContent(FootnoteId footnoteId) {
        ParsedFootnote parsedFootnote = parsedFootnotes.get(footnoteId);
        if (parsedFootnote == null) {
            throw new IllegalArgumentException("can't find footnote with id <" + footnoteId + ">");
        }

        return parsedFootnote.docElement().contentToListOfMaps();
    }
}

