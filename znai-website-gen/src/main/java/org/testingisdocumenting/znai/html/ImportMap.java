package org.testingisdocumenting.znai.html;

import org.testingisdocumenting.znai.website.WebResource;

import java.util.Map;
import java.util.stream.Collectors;

public class ImportMap {
    private final Map<String, WebResource> map;
    public ImportMap(Map<String, String> map) {
        this.map = map.entrySet().stream().map(e -> Map.entry(e.getKey(), WebResource.fromResource(e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public String render(String documentationId) {
        String imports = map.entrySet().stream()
                .map(entry -> "        \"" + entry.getKey() + "\": \"" + entry.getValue().pathForHtml(documentationId) + "\"")
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
