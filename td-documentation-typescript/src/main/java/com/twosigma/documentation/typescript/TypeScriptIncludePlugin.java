package com.twosigma.documentation.typescript;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class TypeScriptIncludePlugin implements IncludePlugin {
    private Path fullPath;
    private MarkdownParser markdownParser;

    @Override
    public String id() {
        return "typescript";
    }

    @Override
    public IncludePlugin create() {
        return new TypeScriptIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        markdownParser = componentsRegistry.markdownParser();
        fullPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());

        List<Map<String, ?>> entries = ParserSingleton.INSTANCE.parsedFile(fullPath);

        String propertiesOf = pluginParams.getOpts().get("propertiesOf", "");
        if (! propertiesOf.isEmpty()) {
            return extractPropertiesOf(propertiesOf, entries);
        }

        throw new UnsupportedOperationException("only support propertiesOf");
    }

    @SuppressWarnings("unchecked")
    private PluginResult extractPropertiesOf(String propertiesOf, List<Map<String, ?>> entries) {
        TypeScriptCode typeScriptCode = new TypeScriptCode(entries);
        TypeScriptType type = typeScriptCode.findType(propertiesOf);

        List<Map<String, ?>> parameters = type.getProperties().stream()
                .map(this::propertyToMap)
                .collect(toList());

        return PluginResult.docElement("ApiParameters", Collections.singletonMap("parameters", parameters));
    }

    private Map<String, ?> propertyToMap(TypeScriptProperty p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", p.getName());
        map.put("type", p.getType());

        MarkupParserResult markupParserResult = markdownParser.parse(fullPath, p.getDocumentation());
        map.put("description", markupParserResult.getDocElement()
                .getContent().stream().map(DocElement::toMap).collect(toList()));

        return map;
    }

    private static class ParserSingleton {
        private final static TypescriptNodeBasedParser INSTANCE = new TypescriptNodeBasedParser();
    }

    public static void main(String[] args) {
        List<Map<String, ?>> parsedFile = ParserSingleton.INSTANCE.parsedFile(Paths.get("/Users/mykolagolubyev/work/TestingIsDocumenting/td-documentation-typescript/src/main/javascript/test-data/Sample.ts"));
        System.out.println(parsedFile);
    }
}
