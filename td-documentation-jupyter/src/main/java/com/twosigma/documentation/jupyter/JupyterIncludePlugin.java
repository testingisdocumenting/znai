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
    private ParserHandler markdownParserHandler;

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
        markdownParserHandler = parserHandler;
        codeTokenizer = componentsRegistry.codeTokenizer();

        isStoryFirst = pluginParams.getOpts().get("storyFirst", false);

        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        path = resourcesResolver.fullPath(pluginParams.getFreeParam());

        JupyterNotebook notebook = new JupyterParserVer4()
                .parse(JsonUtils.deserializeAsMap(resourcesResolver.textContent(path)));
        lang = notebook.getLang();

        notebook.getCells().forEach(this::processCell);
        return PluginResult.docElements(Stream.empty());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(path));
    }

    private void processCell(JupyterCell cell) {
        processMarkdownCell(cell);

        if (isStoryFirst) {
            processOutputFromCell(cell);
            processInputFromCell(cell);
        } else {
            processInputFromCell(cell);
            processOutputFromCell(cell);
        }
    }

    private void processMarkdownCell(JupyterCell cell) {
        if (!cell.getType().equals(MARKDOWN_TYPE)) {
            return;
        }

        markdownParser.parse(path, cell.getInput(), markdownParserHandler);
    }

    private void processInputFromCell(JupyterCell cell) {
        if (isMarkdown(cell)) {
            return;
        }

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("cellType", cell.getType());
        props.putAll(convertInputData(cell));

        if (isStoryFirst) {
            props.putAll(createMetaRight());
        }

        addCell(props);
    }

    private void processOutputFromCell(JupyterCell cell) {
        if (cell.getOutputs().isEmpty()) {
            processEmptyOutput();
        } else {
            cell.getOutputs().forEach(this::processCellOutput);
        }
    }

    private void processCellOutput(JupyterOutput output) {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("cellType", "output");
        props.putAll(convertOutputData(output));

        if (!isStoryFirst) {
            props.putAll(createMetaRight());
        }

        addCell(props);
    }

    private void processEmptyOutput() {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("cellType", "empty-output");
        props.putAll(createMetaRight());

        addCell(props);
    }

    private void addCell(Map<String, ?> props) {
        markdownParserHandler.onCustomNode("JupyterCell", props);
    }

    private boolean isMarkdown(JupyterCell cell) {
        return cell.getType().equals(MARKDOWN_TYPE);
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
