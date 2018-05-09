package com.twosigma.documentation.extensions.api;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;
import com.twosigma.documentation.extensions.table.CsvParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.parser.table.MarkupTableData;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ApiParametersFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "api-parameters";
    }

    @Override
    public FencePlugin create() {
        return new ApiParametersFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        MarkdownParser markdownParser = componentsRegistry.markdownParser();

        MarkupTableData tableData = CsvParser.parseWithHeader(content, "name", "type", "description");
        List<Map<String, Object>> parameters = tableData.mapRows(row -> {
            Map<String, Object> p = new LinkedHashMap<>();
            p.put("name", row.get(0).toString());
            p.put("type", row.get(1).toString());

            MarkupParserResult markupParserResult = markdownParser.parse(markupPath, row.get(2));
            p.put("description", markupParserResult.getDocElement().contentToListOfMaps());

            return p;
        }).collect(toList());

        return PluginResult.docElement("ApiParameters", Collections.singletonMap("parameters", parameters));
    }
}
