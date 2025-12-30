package org.testingisdocumenting.znai.html;

import java.util.Map;
import java.util.stream.Collectors;

public class ImportMap implements RenderSupplier{
    private final Map<String, String> map;
    public ImportMap(Map<String, String> map) {
        this.map = Map.copyOf(map);
    }
    @Override
    public String render() {
        String imports = map.entrySet().stream()
                .map(entry -> "        \"" + entry.getKey() + "\": \"" + entry.getValue() + "\"")
                .collect(Collectors.joining(",\n"));

        return "<script type=\"importmap\">\n" +
                "    {\n" +
                "      \"imports\": {\n" +
                imports +
                "      }\n" +
                "    }\n" +
                "</script>";
    }
}
