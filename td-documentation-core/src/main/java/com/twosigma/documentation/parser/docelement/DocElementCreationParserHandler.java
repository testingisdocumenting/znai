package com.twosigma.documentation.parser.docelement;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.Plugin;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.Plugins;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.inlinedcode.InlinedCodePlugin;
import com.twosigma.documentation.parser.PageSectionIdTitle;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class DocElementCreationParserHandler implements ParserHandler {
    private final ComponentsRegistry componentsRegistry;
    private final Path path;
    private final List<AuxiliaryFile> auxiliaryFiles;

    private final List<DocElement> paragraphs;

    private final DocElement docElement;
    private final Deque<DocElement> elementsStack;

    public DocElementCreationParserHandler(ComponentsRegistry componentsRegistry, Path path) {
        this.componentsRegistry = componentsRegistry;
        this.path = path;
        this.paragraphs = new ArrayList<>();
        this.auxiliaryFiles = new ArrayList<>();

        this.docElement = new DocElement("page");
        this.elementsStack = new ArrayDeque<>();
        this.elementsStack.add(docElement);
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public List<AuxiliaryFile> getAuxiliaryFiles() {
        return auxiliaryFiles;
    }

    @Override
    public void onSectionStart(String title) {
        start(DocElementType.SECTION, "title", title, "id", new PageSectionIdTitle(title).getId());
    }

    @Override
    public void onSectionEnd() {
        end();
    }

    @Override
    public void onSubHeadingStart(int level) {
        start(DocElementType.SUB_HEADING, "level", level);
    }

    @Override
    public void onSubHeadingEnd(int level) {
        end();
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
    public void onInlinedCode(String inlinedCode) {
        append(DocElementType.INLINED_CODE, "code", inlinedCode);
    }

    @Override
    public void onLinkStart(String url) {
        start(DocElementType.LINK, "url", url);
    }

    @Override
    public void onLinkEnd() {
        end();
    }

    @Override
    public void onImage(String title, String destination, String alt) {
        BufferedImage image = componentsRegistry.includeResourceResolver().imageContent(destination);
        append(DocElementType.IMAGE, "title", title, "destination", destination, "alt", alt, "inlined", true,
                "width", image.getWidth(),
                "height", image.getHeight());

        if (! destination.startsWith("http")) {
            auxiliaryFiles.add(AuxiliaryFile.runTime(componentsRegistry.includeResourceResolver().fullPath(destination)));
        }
    }

    @Override
    public void onSnippet(String lang, String lineNumber, String snippet) {
        Map<String, Object> snippetProps = CodeSnippetsProps.create(componentsRegistry.codeTokenizer(), lang, snippet);
        snippetProps.put("lineNumber", lineNumber);

        append(DocElementType.SNIPPET, snippetProps);
    }

    @Override
    public void onThematicBreak() {
        append(DocElementType.THEMATIC_BREAK);
    }

    @Override
    public void onIncludePlugin(PluginParams pluginParams) {
        IncludePlugin includePlugin = Plugins.includePluginById(pluginParams.getPluginId());
        processPlugin(includePlugin, (p) -> p.process(componentsRegistry, path, pluginParams));
    }

    @Override
    public void onFencePlugin(PluginParams pluginParams, String content) {
        FencePlugin fencePlugin = Plugins.fencePluginById(pluginParams.getPluginId());
        processPlugin(fencePlugin, (p) -> p.process(componentsRegistry, path, pluginParams, content));
    }

    @Override
    public void onInlinedCodePlugin(PluginParams pluginParams) {
        InlinedCodePlugin inlinedCodePlugin = Plugins.inlinedCodePluginById(pluginParams.getPluginId());
        processPlugin(inlinedCodePlugin, (p) -> p.process(componentsRegistry, path, pluginParams));
    }

    private <E extends Plugin> void processPlugin(E plugin, Function<E, PluginResult> processFunc) {
        try {
            PluginResult result = processFunc.apply(plugin);

            plugin.auxiliaryFiles(componentsRegistry).forEach(auxiliaryFiles::add);

            List<DocElement> docElements = result.getDocElements();
            if (docElements.isEmpty()) {
                return;
            }

            // if element is a section itself we need to close all the current sections and paragraphs
            if (docElements.get(0).getType().equals(DocElementType.SECTION)) {
                while (elementsStack.size() > 1) {
                    end();
                }
            }

            docElements.forEach(this::append);
        } catch (Exception e) {
            throw new RuntimeException("failure during processing include plugin '" + plugin.id() + "': " + e.getMessage(), e);
        }
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
        if (paragraph.getContent().size() > 1 || paragraph.getContent().size() == 0) {
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
}

