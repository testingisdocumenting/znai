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

package com.twosigma.znai.jupyter;

import com.twosigma.znai.codesnippets.CodeSnippetsProps;
import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.parser.commonmark.MarkdownParser;
import com.twosigma.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JupyterIncludePlugin implements IncludePlugin {
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
        if (!cell.getType().equals(JupyterCell.MARKDOWN_TYPE)) {
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
        return cell.getType().equals(JupyterCell.MARKDOWN_TYPE);
    }

    private Map<String, ?> createMetaRight() {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("rightSide", true);

        return Collections.singletonMap("meta", meta);
    }

    private Map<String, Object> convertInputData(JupyterCell cell) {
        switch (cell.getType()) {
            case JupyterCell.CODE_TYPE:
                return CodeSnippetsProps.create(lang, cell.getInput());
            default:
                return Collections.singletonMap(JupyterOutput.TEXT_FORMAT, cell.getInput());
        }
    }

    private Map<String, Object> convertOutputData(JupyterOutput output) {
        return Collections.singletonMap(output.getFormat(), output.getContent());
    }
}
