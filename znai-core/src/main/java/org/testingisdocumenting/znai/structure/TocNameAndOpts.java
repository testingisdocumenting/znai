/*
 * Copyright 2022 znai maintainers
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

import org.testingisdocumenting.znai.utils.JsonUtils;
import org.testingisdocumenting.znai.utils.NameUtils;

import java.util.Collections;
import java.util.Map;

public class TocNameAndOpts {
    private final String givenName;
    private final Map<String, ?> opts;

    private String humanReadableName;

    public TocNameAndOpts(String trimmed) {
        int openBracketIdx = trimmed.indexOf('{');

        boolean hasOpenBracket = openBracketIdx != -1;
        this.givenName = hasOpenBracket ? trimmed.substring(0, openBracketIdx).trim() : trimmed;
        this.opts = hasOpenBracket ? extractOpts(trimmed, openBracketIdx) : Collections.emptyMap();

        this.humanReadableName = buildHumanReadableName();
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public void setHumanReadableName(String humanReadableName) {
        this.humanReadableName = humanReadableName;
    }

    public String getGivenName() {
        return givenName;
    }

    public Map<String, ?> getOpts() {
        return opts;
    }

    private Map<String, ?> extractOpts(String trimmed, int openBracketIdx) {
        int closeBracketIdx = trimmed.indexOf('}');
        if (closeBracketIdx == -1) {
            throw new IllegalArgumentException("can't find closing } for options: " + trimmed);
        }

        return JsonUtils.deserializeAsMap(trimmed.substring(openBracketIdx, closeBracketIdx + 1));
    }

    private String buildHumanReadableName() {
        Object title = opts.get("title");
        if (title != null) {
            return title.toString();
        }

        return NameUtils.dashToCamelCaseWithSpaces(givenName);
    }
}
