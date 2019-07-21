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

package com.twosigma.znai.extensions;

import com.twosigma.znai.parser.docelement.DocElement;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PluginResult {
    private List<DocElement> docElements;

    private PluginResult(DocElement docElements) {
        this.docElements = Collections.singletonList(docElements);
    }

    private PluginResult(List<DocElement> docElements) {
        this.docElements = docElements;
    }

    public static PluginResult docElements(Stream<DocElement> elements) {
        return new PluginResult(elements.collect(Collectors.toList()));
    }

    public static PluginResult docElement(DocElement element) {
        return new PluginResult(element);
    }

    public static PluginResult docElement(String type, Map<String, ?> props) {
        DocElement docElement = new DocElement(type);
        props.forEach(docElement::addProp);

        return new PluginResult(docElement);
    }

    public List<DocElement> getDocElements() {
        return docElements;
    }
}
