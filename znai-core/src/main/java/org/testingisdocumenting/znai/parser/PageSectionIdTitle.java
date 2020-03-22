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

package com.twosigma.znai.parser;

import com.twosigma.znai.utils.NameUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class PageSectionIdTitle {
    private String title;
    private String id;

    public PageSectionIdTitle(String title) {
        this.title = title;
        this.id = NameUtils.idFromTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", getTitle());
        result.put("id", getId());

        return result;
    }
}
