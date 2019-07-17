package com.twosigma.znai.parser.docelement;

import com.twosigma.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Represents model to be used by ReactJS for rendering.
 * @see DocElementType
 */
public class DocElement {
    private Map<String, Object> props;
    private List<DocElement> content;
    private String type;

    public DocElement(String type) {
        this.type = type;
        props = new LinkedHashMap<>();

        content = new ArrayList<>();
    }

    public DocElement(String type, Object... keyValues) {
        this(type);
        Map<String, Object> props = CollectionUtils.createMap(keyValues);
        props.forEach(this::addProp);
    }

    public void addProp(String key, Object value) {
        props.put(key, value);
    }

    public void addChild(DocElement element) {
        content.add(element);
    }

    public void removeChild(DocElement element) {
        content.remove(element);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Object getProp(String name) {
        return props.get(name);
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public List<DocElement> getContent() {
        return content;
    }

    public List<Map<String, Object>> contentToListOfMaps() {
        return content.stream().map(DocElement::toMap).collect(toList());
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new LinkedHashMap<>(props);
        result.put("type", type);
        if (! content.isEmpty()) {
            result.put("content", contentToListOfMaps());
        }

        return result;
    }

    @Override
    public String toString() {
        return toMap().toString();
    }
}
