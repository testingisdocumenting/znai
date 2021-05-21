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

package org.testingisdocumenting.znai.structure;

import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.LinkedHashMap;
import java.util.Map;

public class Footer {
    private final DocElement docElement;

    public Footer(DocElement docElement) {
        this.docElement = docElement;
    }

    public DocElement getDocElement() {
        return docElement;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> props = new LinkedHashMap<>();

        props.put("type", "Footer");
        props.put("content", ((Map<String, ?>) getDocElement().toMap()).get("content"));

        return props;
    }
}
