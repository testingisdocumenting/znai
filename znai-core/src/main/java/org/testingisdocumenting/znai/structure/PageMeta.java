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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * custom information associated with a single page of documentation
 */
public class PageMeta {
    private final Map<String, List<String>> meta;

    public PageMeta() {
        this.meta = Collections.emptyMap();
    }

    public PageMeta(Map<String, List<String>> meta) {
        this.meta = meta;
    }

    public boolean hasValue(String key) {
        return meta.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <E> E getSingleValue(String key) {
        List<String> multiValues = meta.get(key);
        if (multiValues != null) {
            return (E) multiValues.get(0);
        }

        return null;
    }

    public Map<String, ?> toMap() {
        return meta;
    }
}
