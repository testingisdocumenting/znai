/*
 * Copyright 2022 znai maintainers
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

import org.testingisdocumenting.znai.codesnippets.CodeSnippetsProps;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class JupyterIncludePlugin implements IncludePlugin {
    private static final String STORY_FIRST_KEY = "storyFirst";
    private static final String INCLUDE_SECTION_KEY = "includeSection";
    private static final String EXCLUDE_SECTION_TITLE_KEY = "excludeSectionTitle";
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
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition params = new PluginParamsDefinition();
        params.add(STORY_FIRST_KEY, PluginParamType.BOOLEAN, "put output cells first, before input", "true");
        params.add(INCLUDE_SECTION_KEY, PluginParamType.LIST_OR_SINGLE_STRING, "only include specified section by title", "Example of Data setup");
        params.add(EXCLUDE_SECTION_TITLE_KEY, PluginParamType.BOOLEAN, "when include section key is used, excludes the matched title", "true");

        return params;
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        markdownParser = componentsRegistry.markdownParser();
        markdownParserHandler = parserHandler;

        isStoryFirst = pluginParams.getOpts().get(STORY_FIRST_KEY, false);
        List<String> includeSection = pluginParams.getOpts().getList(INCLUDE_SECTION_KEY);
        Boolean excludeSectionTitle = pluginParams.getOpts().get(EXCLUDE_SECTION_TITLE_KEY, false);

        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        path = resourcesResolver.fullPath(pluginParams.getFreeParam());

        JupyterNotebook notebook = new JupyterParserVer4()
                .parse(JsonUtils.deserializeAsMap(resourcesResolver.textContent(path)));
        lang = notebook.getLang();

        List<JupyterCell> cells =
                !includeSection.isEmpty() ?
                        collectCells(notebook.getCells(), includeSection, excludeSectionTitle) :
                        notebook.getCells();

        cells.forEach(this::processCell);
        return PluginResult.docElements(Stream.empty());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(path));
    }

    private List<JupyterCell> collectCells(List<JupyterCell> cells, List<String> includeSections, Boolean excludeSectionTitle) {
        List<JupyterCell> result = new ArrayList<>();
        for (String includeSection : includeSections) {
            List<JupyterCell> filtered = JupyterCellFilter.fromSection(cells, includeSection, excludeSectionTitle);
            if (filtered.isEmpty()) {
                throw new RuntimeException("No cells found for include section: \"" + includeSection + "\"");
            }
            result.addAll(filtered);
        }

        return result;
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

        markdownParser.parse(path, markdownParserHandler, cell.getInput());
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
        if (cell.getType().equals(JupyterCell.CODE_TYPE)) {
            return CodeSnippetsProps.create(lang, cell.getInput());
        }
        return Collections.singletonMap(JupyterOutput.TEXT_FORMAT, cell.getInput());
    }

    private Map<String, Object> convertOutputData(JupyterOutput output) {
        return Collections.singletonMap(output.getFormat(), output.getContent());
    }
}
