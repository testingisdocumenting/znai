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

package org.testingisdocumenting.znai.extensions.file;

import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class TextContentExtractor {
    private TextContentExtractor() {
    }

    public static String extractText(String content, PluginParamsOpts opts) {
        if (opts.isEmpty()) {
            return content;
        }

        Text text = new Text(content);
        Text croppedAtStart = cropStart(text, opts);
        Text croppedAtEnd = cropEnd(croppedAtStart, opts);

        Text withExcludedStartEnd = excludeStartEnd(croppedAtEnd, opts);
        Text withIncludeRegexp = includeRegexp(withExcludedStartEnd, opts);
        Text withExcludedRegexp = excludeRegexp(withIncludeRegexp, opts);

        return StringUtils.stripIndentation(withExcludedRegexp.toString());
    }

    private static Text cropStart(Text text, PluginParamsOpts opts) {
        String startLine = opts.get("startLine");
        if (startLine == null) {
            return text;
        }

        return text.startingWithLineContaining(startLine);
    }

    private static Text cropEnd(Text text, PluginParamsOpts opts) {
        Number numberOfLines = opts.get("numberOfLines");
        if (numberOfLines != null) {
            return text.limitTo(numberOfLines);
        }

        String endLine = opts.get("endLine");
        if (endLine != null) {
            return text.limitToLineContaining(endLine);
        }

        return text;
    }

    private static Text excludeStartEnd(Text text, PluginParamsOpts opts) {
        Boolean exclude = opts.get("excludeStartEnd", false);
        if (!exclude) {
            return text;
        }

        return text.cropOneLineFromStartAndEnd();
    }

    private static Text includeRegexp(Text text, PluginParamsOpts opts) {
        List<String> includeRegexps = opts.getList("includeRegexp");
        if (includeRegexps.isEmpty()) {
            return text;
        }

        return text.includeRegexp(createListOfPatterns(includeRegexps));
    }

    private static Text excludeRegexp(Text text, PluginParamsOpts opts) {
        List<String> excludeRegexps = opts.getList("excludeRegexp");
        if (excludeRegexps.isEmpty()) {
            return text;
        }

        return text.excludeRegexp(createListOfPatterns(excludeRegexps));
    }

    private static List<Pattern> createListOfPatterns(List<String> regexps) {
        return regexps.stream().map(Pattern::compile).collect(toList());
    }

    private static class Text {
        private final List<String> lines;

        public Text(String text) {
            this.lines = Arrays.asList(text.split("\n"));
        }

        public Text(List<String> lines) {
            this.lines = lines;
        }

        Text startingWithLineContaining(String subLine) {
            int lineIdx = findLineIdxContaining(subLine);
            return new Text(lines.subList(lineIdx, lines.size()));
        }

        Text limitToLineContaining(String subLine) {
            int lineIdx = findLineIdxContaining(subLine);
            return new Text(lines.subList(0, lineIdx + 1));
        }

        Text limitTo(Number numberOfLines) {
            return new Text(lines.subList(0, numberOfLines.intValue()));
        }

        Text cropOneLineFromStartAndEnd() {
            return new Text(lines.subList(1, lines.size() - 1));
        }

        Text includeRegexp(List<Pattern> regexps) {
            return new Text(lines.stream()
                    .filter(line -> regexps.stream().anyMatch(r -> r.matcher(line).find()))
                    .collect(Collectors.toList()));
        }

        Text excludeRegexp(List<Pattern> regexps) {
            return new Text(lines.stream()
                    .filter(line -> regexps.stream().noneMatch(r -> r.matcher(line).find()))
                    .collect(Collectors.toList()));
        }

        private int findLineIdxContaining(String subLine) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains(subLine)) {
                    return i;
                }
            }

            throw new IllegalArgumentException("<there is no line containing " + subLine + " in:\n" + toString());
        }

        @Override
        public String toString() {
            return String.join("\n", lines);
        }
    }
}
