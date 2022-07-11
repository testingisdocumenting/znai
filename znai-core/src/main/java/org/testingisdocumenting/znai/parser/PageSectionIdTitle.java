/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.parser;

import org.testingisdocumenting.znai.utils.NameUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class PageSectionIdTitle {
    private final String title;
    private final String id;
    private final Map<String, ?> headingProps;

    public PageSectionIdTitle(String title, Map<String, ?> headingProps) {
        this.title = title;
        this.id = NameUtils.idFromTitle(title);
        this.headingProps = headingProps;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public Map<String, ?> getHeadingProps() {
        return headingProps;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", getTitle());
        result.put("id", getId());
        result.putAll(getHeadingProps());

        return result;
    }
}
