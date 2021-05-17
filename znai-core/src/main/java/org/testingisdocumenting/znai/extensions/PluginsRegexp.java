/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginsRegexp {
    public static final Pattern INCLUDE_PLUGIN_PATTERN = Pattern.compile("^\\s*:include-(\\S+)+:\\s*(.*)$");
    public static final Pattern INLINED_CODE_PATTERN = Pattern.compile("^:([a-zA-Z-_]+):\\s*(.*)");

    public static class IdAndParams {
        private final String id;
        private final String params;

        public IdAndParams(String id, String params) {
            this.id = id;
            this.params = params;
        }

        public String getId() {
            return id;
        }

        public String getParams() {
            return params;
        }
    }

    private PluginsRegexp() {
    }

    public static PluginsRegexp.IdAndParams parseIncludePlugin(CharSequence line) {
        return parse(INCLUDE_PLUGIN_PATTERN, line);
    }

    public static PluginsRegexp.IdAndParams parseInlinedCodePlugin(CharSequence line) {
        return parse(INLINED_CODE_PATTERN, line);
    }

    public static PluginsRegexp.IdAndParams parse(Pattern pattern, CharSequence line) {
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            return null;
        }

        return new PluginsRegexp.IdAndParams(matcher.group(1).trim(), matcher.group(2).trim());
    }
}
