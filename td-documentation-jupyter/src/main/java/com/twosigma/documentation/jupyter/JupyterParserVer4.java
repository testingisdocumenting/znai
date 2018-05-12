package com.twosigma.documentation.jupyter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.twosigma.documentation.jupyter.JupyterCell.CODE_TYPE;
import static com.twosigma.documentation.jupyter.JupyterCell.MARKDOWN_TYPE;
import static com.twosigma.documentation.jupyter.JupyterOutput.HTML_FORMAT;
import static com.twosigma.documentation.jupyter.JupyterOutput.TEXT_FORMAT;
import static java.util.stream.Collectors.toList;

public class JupyterParserVer4 implements JupyterParser {

    public JupyterParserVer4() {
    }

    @Override
    public JupyterNotebook parse(Map<String, ?> json) {
        List<?> cells = (List<?>) json.get("cells");

        return new JupyterNotebook(parseLang(json),
                cells.stream().map(this::parseCell).collect(toList()));
    }

    @SuppressWarnings("unchecked")
    private JupyterCell parseCell(Object o) {
        Map<String, ?> cellContent = (Map<String, ?>) o;

        String type = cellContent.get("cell_type").toString();

        switch (type) {
            case CODE_TYPE:
                return parseCodeCell(cellContent);
            case MARKDOWN_TYPE:
                return parseMarkdownCell(cellContent);
        }

        return new JupyterCell("unknown", "", Collections.emptyList());
    }

    private JupyterCell parseMarkdownCell(Map<String, ?> cellContent) {
        return new JupyterCell(MARKDOWN_TYPE, joinLines(cellContent.get("source")), Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    private JupyterCell parseCodeCell(Map<String, ?> cellContent) {
        String input = joinLines(cellContent.get("source"));

        List<Map<String, ?>> outputsContent = (List<Map<String, ?>>) cellContent.get("outputs");
        List<JupyterOutput> outputs = outputsContent.stream().map(this::parseOutput).collect(toList());

        return new JupyterCell(CODE_TYPE, input, outputs);
    }

    @SuppressWarnings("unchecked")
    private JupyterOutput parseOutput(Map<String, ?> outputContent) {
        String type = outputContent.get("output_type").toString();
        switch (type) {
            case "stream":
                return new JupyterOutput(type, TEXT_FORMAT,
                        joinLines(outputContent.get("text")));
            case "display_data":
                return new JupyterOutput(type, HTML_FORMAT,
                        parseDisplayData((Map<String, ?>) outputContent.get("data")));
        }

        return new JupyterOutput(type, TEXT_FORMAT, "<can't parse " + type + ">");
    }

    private String parseDisplayData(Map<String, ?> data) {
        Object html = data.get("text/html");
        if (html != null) {
            return joinLines(html);
        }

        Object text = data.get("text/plain");
        return text != null ? joinLines(text) : "";
    }

    @SuppressWarnings("unchecked")
    private String joinLines(Object listOfLines) {
        List<String> lines = (List<String>) listOfLines;
        return String.join("", lines);
    }

    @SuppressWarnings("unchecked")
    private String parseLang(Map<String, ?> json) {
        Map<String, ?> metadata = (Map<String, ?>) json.get("metadata");
        Map<String, ?> kernelspec = (Map<String, ?>) metadata.get("kernelspec");

        return kernelspec.get("language").toString();
    }
}
