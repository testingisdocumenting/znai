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

package org.testingisdocumenting.znai.extensions.include;

import org.testingisdocumenting.znai.extensions.PluginParams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncludePluginParser {
    private static final Pattern PATTERN = Pattern.compile(":include-(\\S+)+:(.*)$");

    private IncludePluginParser() {
    }

    public static PluginParams parse(String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("To define include plugin use\n:" +
                    "include-plugin-id: free form value {optional: keyValues}\n" +
                    "Got: " + line);
        }

        return new PluginParams(matcher.group(1).trim(), matcher.group(2).trim());
    }
}
