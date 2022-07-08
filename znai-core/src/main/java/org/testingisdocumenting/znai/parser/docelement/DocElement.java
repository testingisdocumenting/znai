/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.parser.docelement;

import org.testingisdocumenting.znai.utils.CollectionUtils;

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
    private final Map<String, Object> props;
    private final List<DocElement> content;
    private String type;

    public DocElement(String type) {
        this.type = type;
        props = new LinkedHashMap<>();

        content = new ArrayList<>();
    }

    public static DocElement withPropsMap(String type, Map<String, Object> props) {
        DocElement docElement = new DocElement(type);
        docElement.addProps(props);

        return docElement;
    }

    public DocElement(String type, Object... keyValues) {
        this(type);
        Map<String, Object> props = CollectionUtils.createMap(keyValues);
        props.forEach(this::addProp);
    }

    public void addProp(String key, Object value) {
        props.put(key, value);
    }

    public void addProps(Map<String, ?> props) {
        this.props.putAll(props);
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
