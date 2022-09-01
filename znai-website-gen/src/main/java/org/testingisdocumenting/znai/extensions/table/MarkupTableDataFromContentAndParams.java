/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.table;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.parser.table.*;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

class MarkupTableDataFromContentAndParams {
    private final ComponentsRegistry componentsRegistry;
    private final String contentToParse;
    private final String mappingFileName;
    private final Path mappingPath;
    private final MarkupTableData markupTableData;

    MarkupTableDataFromContentAndParams(ComponentsRegistry componentsRegistry, PluginParams pluginParams, String contentToParse) {
        this.componentsRegistry = componentsRegistry;
        this.contentToParse = contentToParse;
        this.mappingFileName = pluginParams.getOpts().get("mappingPath", "");
        this.mappingPath = mappingFileName.isEmpty() ? null : componentsRegistry.resourceResolver().fullPath(mappingFileName);

        this.markupTableData = parse();
    }

    public MarkupTableData getMarkupTableData() {
        return markupTableData;
    }

    public Stream<String> columnNamesStream() {
        return markupTableData.columnNamesStream();
    }

    Stream<AuxiliaryFile> mappingAuxiliaryFile() {
        return mappingPath == null ? Stream.empty() : Stream.of(AuxiliaryFile.builtTime(mappingPath));
    }

    private MarkupTableData parse() {
        return (isJson() ?
                JsonTableParser.parse(contentToParse) :
                CsvTableParser.parse(contentToParse)).mapValues(createMapping());
    }

    private boolean isJson() {
        return contentToParse.trim().startsWith("[");
    }

    private MarkupTableDataMapping createMapping() {
        return new MapBasedMarkupTableMapping(mappingPath == null ?
                Collections.emptyMap():
                createMappingFromFileContent());
    }

    private Map<Object, Object> createMappingFromFileContent() {
        MarkupTableData tableData = CsvTableParser.parseWithHeader(componentsRegistry.resourceResolver().textContent(mappingFileName),
                "from", "to");
        return Collections.unmodifiableMap(tableData.toKeyValue());
    }
}
