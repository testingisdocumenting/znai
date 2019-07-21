/*
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

package com.twosigma.znai.diagrams.slides;

import com.twosigma.znai.parser.docelement.DocElement;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Each slide reveals or re-highlight already revealed diagram items. Additionally context information can be rendered
 * to explain that particular part of a flow or a dependency
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
