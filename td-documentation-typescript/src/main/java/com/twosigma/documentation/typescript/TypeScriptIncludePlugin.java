package com.twosigma.documentation.typescript;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;

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
    private TypeScriptCode typeScriptCode;

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
        typeScriptCode = new TypeScriptCode(entries);

        String propertiesOf = pluginParams.getOpts().get("propertiesOf", "");
        if (! propertiesOf.isEmpty()) {
            return extractPropertiesOf(propertiesOf);
        }

        String jsxElementsFrom = pluginParams.getOpts().get("jsxElementsFrom", "");
        if (!jsxElementsFrom.isEmpty()) {
            return extractJsxElementsFrom(jsxElementsFrom);
        }

        throw new UnsupportedOperationException("only support propertiesOf");
    }

    @SuppressWarnings("unchecked")
    private PluginResult extractPropertiesOf(String propertiesOf) {
        TypeScriptType type = typeScriptCode.findType(propertiesOf);

        List<Map<String, ?>> parameters = type.getProperties().stream()
                .map(this::propertyToMap)
                .collect(toList());

        return PluginResult.docElement("ApiParameters", Collections.singletonMap("parameters", parameters));
    }

    private PluginResult extractJsxElementsFrom(String jsxElementsFrom) {
        String[] path = jsxElementsFrom.split("\\.");
        if (path.length == 1) {
            return extractJsxElementsFromFunction(path[0]);
        } else if (path.length == 2) {
            return extractJsxElementsFromMethod(path[0], path[1]);
        }

        throw new RuntimeException("pass either function name or method name to extract jsx elements from");
    }

    private PluginResult extractJsxElementsFromMethod(String typeName, String methodName) {
        throw new UnsupportedOperationException("extracting jsx elements from type's method is not supported yet");
    }

    private PluginResult extractJsxElementsFromFunction(String funcName) {
        TypeScriptFunction function = typeScriptCode.findFunction(funcName);
        List<? extends Map<String, ?>> declarations = function.getJsxDeclarations().stream()
                .map(TypeScriptJsxDeclaration::toMap)
                .collect(toList());

        return PluginResult.docElement( "JsxGroup", Collections.singletonMap("declarations", declarations));
    }

    // we convert map here as we need to convert markdown to doc elements
    private Map<String, ?> propertyToMap(TypeScriptProperty p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", p.getName());
        map.put("type", p.getType());

        MarkupParserResult markupParserResult = markdownParser.parse(fullPath, p.getDocumentation());
        map.put("description", markupParserResult.getDocElement().contentToListOfMaps());

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
