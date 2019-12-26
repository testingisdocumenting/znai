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

package com.twosigma.znai.reference;

import java.util.Collections;
import java.util.Map;

/**
 * maintains a list of references (code references, text references, etc) and page-urls associated with them
 */
public class DocReferences {
    private final Map<String, Object> references;

    public DocReferences(Map<String, Object> references) {
        this.references = Collections.unmodifiableMap(references);
    }

    public Map<String, Object> toMap() {
        return references;
    }
}
