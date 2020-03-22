/*
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

package org.testingisdocumenting.znai.jupyter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.znai.jupyter.JupyterCell.CODE_TYPE;
import static org.testingisdocumenting.znai.jupyter.JupyterCell.MARKDOWN_TYPE;
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
                return new JupyterOutput(JupyterOutput.TEXT_FORMAT, joinLines(outputContent.get("text")));
            default:
                return parseOutputData((Map<String, ?>) outputContent.get("data"));
        }
    }

    private JupyterOutput parseOutputData(Map<String, ?> data) {
        Object html = data.get("text/html");
        if (html != null) {
            return new JupyterOutput(JupyterOutput.HTML_FORMAT, joinLines(html));
        }

        Object svg = data.get("image/svg+xml");
        if (svg != null) {
            return new JupyterOutput(JupyterOutput.SVG_FORMAT, joinLines(svg));
        }

        Object img = data.get("image/png");
        if (img != null) {
            return new JupyterOutput(JupyterOutput.IMG_FORMAT, img.toString());
        }

        Object text = data.get("text/plain");
        return new JupyterOutput(JupyterOutput.TEXT_FORMAT, joinLines(text));
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
