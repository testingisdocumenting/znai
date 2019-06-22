package com.twosigma.documentation.diagrams.slides;

import com.twosigma.documentation.parser.docelement.DocElement;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Each slide reveals or re-highlight already revealed diagram items. Additionally context information can be rendered
 * to explain that particular part of a flow or a dependency
 * @author mykola
 */
public class DiagramSlide {
    private List<String> ids;
    private List<DocElement> content;

    public DiagramSlide(List<String> ids, List<DocElement> content) {
        this.ids = ids;
        this.content = content;
    }

    public List<String> getIds() {
        return Collections.unmodifiableList(ids);
    }

    public List<DocElement> getContent() {
        return Collections.unmodifiableList(content);
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("ids", ids);
        result.put("content", content.stream().map(DocElement::toMap).collect(toList()));

        return result;
    }

    @Override
    public String toString() {
        return toMap().toString();
    }
}
