package com.twosigma.documentation.jupyter;

import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.twosigma.documentation.jupyter.JupyterCell.CODE_TYPE;
import static com.twosigma.documentation.jupyter.JupyterCell.MARKDOWN_TYPE;
import static com.twosigma.documentation.jupyter.JupyterOutput.TEXT_FORMAT;

public class JupyterIncludePlugin implements IncludePlugin {
    private CodeTokenizer codeTokenizer;
    private MarkdownParser markdownParser;
    private Path path;
    private String lang;
    private boolean isStoryFirst;

    @Override
    public String id() {
        return "jupyter";
    }

    @Override
    public IncludePlugin create() {
        return new JupyterIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        markdownParser = componentsRegistry.markdownParser();
        codeTokenizer = componentsRegistry.codeTokenizer();

        isStoryFirst = pluginParams.getOpts().get("storyFirst", false);

        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        path = resourcesResolver.fullPath(pluginParams.getFreeParam());

        JupyterNotebook notebook = new JupyterParserVer4()
                .parse(JsonUtils.deserializeAsMap(resourcesResolver.textContent(path)));
        lang = notebook.getLang();

        Stream<DocElement> docElements = notebook.getCells().stream().flatMap(this::convertToDocElements);
        return PluginResult.docElements(docElements);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(path));
    }

    private Stream<DocElement> convertToDocElements(JupyterCell cell) {
        Stream<DocElement> markdownDocElements = createMarkdownDocElements(cell);

        Stream<DocElement> inputElements = createInputDocElement(cell);

        Stream<DocElement> outputElements = cell.getOutputs().isEmpty() ?
                Stream.of(createEmptyOutputDocElement()) :
                cell.getOutputs().stream().map(this::createOutputDocElement);

        return isStoryFirst ?
                Stream.concat(Stream.concat(markdownDocElements, outputElements), inputElements):
                Stream.concat(Stream.concat(markdownDocElements, inputElements), outputElements);
    }

    private Stream<DocElement> createMarkdownDocElements(JupyterCell cell) {
        return cell.getType().equals(MARKDOWN_TYPE) ?
                markdownParser.parse(path, cell.getInput()).getDocElement().getContent().stream() :
                Stream.empty();
    }

    private Stream<DocElement> createInputDocElement(JupyterCell cell) {
        if (isMarkdown(cell)) {
            return Stream.empty();
        }

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("cellType", cell.getType());
        props.putAll(convertInputData(cell));

        if (isStoryFirst) {
            props.putAll(createMetaRight());
        }

        return Stream.of(docElementCell(props));
    }

    private boolean isMarkdown(JupyterCell cell) {
        return cell.getType().equals(MARKDOWN_TYPE);
    }

    private DocElement createOutputDocElement(JupyterOutput output) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("cellType", "output");
        props.putAll(convertOutputData(output));

        if (!isStoryFirst) {
            props.putAll(createMetaRight());
        }

        return docElementCell(props);
    }

    private DocElement createEmptyOutputDocElement() {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("cellType", "empty-output");

        if (!isStoryFirst) {
            props.putAll(createMetaRight());
        }

        return docElementCell(props);
    }

    private DocElement docElementCell(Map<String, Object> props) {
        DocElement element = new DocElement("JupyterCell");
        props.forEach(element::addProp);

        return element;
    }

    private Map<String, ?> createMetaRight() {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("rightSide", true);

        return Collections.singletonMap("meta", meta);
    }

    private Map<String, Object> convertInputData(JupyterCell cell) {
        switch (cell.getType()) {
            case CODE_TYPE:
                return Collections.singletonMap("sourceTokens",
                        codeTokenizer.tokenize(lang, cell.getInput()));

            default:
                return Collections.singletonMap(TEXT_FORMAT, cell.getInput());
        }
    }

    private Map<String, Object> convertOutputData(JupyterOutput output) {
        return Collections.singletonMap(output.getFormat(), output.getContent());
    }
}
