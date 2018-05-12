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
import static com.twosigma.documentation.jupyter.JupyterOutput.HTML_FORMAT;
import static com.twosigma.documentation.jupyter.JupyterOutput.TEXT_FORMAT;

public class JupyterIncludePlugin implements IncludePlugin {
    private CodeTokenizer codeTokenizer;
    private MarkdownParser markdownParser;
    private Path path;
    private String lang;

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
        return Stream.concat(
                createInputDocElement(cell),
                cell.getOutputs().stream().map(this::createOutputDocElement));
    }

    private Stream<DocElement> createInputDocElement(JupyterCell cell) {
        if (cell.getType().equals(MARKDOWN_TYPE)) {
            return markdownParser.parse(path, cell.getInput()).getDocElement().getContent().stream();
        }

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("type", cell.getType());
        props.putAll(convertInputData(cell));

        return Stream.of(docElementCell(props));
    }

    private DocElement createOutputDocElement(JupyterOutput output) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("type", output.getType());
        props.putAll(convertOutputData(output));

        return docElementCell(props);
    }

    private DocElement docElementCell(Map<String, Object> props) {
        DocElement element = new DocElement("JupyterCell");
        props.forEach(element::addProp);

        return element;
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
        switch (output.getFormat()) {
            case HTML_FORMAT:
                return Collections.singletonMap("html", output.getContent());
            case TEXT_FORMAT:
                return Collections.singletonMap("text", output.getContent());
            default:
                return Collections.singletonMap(TEXT_FORMAT, output.getContent());
        }
    }
}
