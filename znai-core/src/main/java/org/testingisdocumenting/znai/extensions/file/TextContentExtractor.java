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

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class TextContentExtractor {
    static final String SURROUNDED_BY_KEY = "surroundedBy";
    static final String SURROUNDED_BY_SEPARATOR_KEY = "surroundedBySeparator";

    static final String START_LINE_KEY = "startLine";
    static final String END_LINE_KEY = "endLine";
    static final String NUMBER_OF_LINES_KEY = "numberOfLines";
    static final String EXCLUDE_START_END_KEY = "excludeStartEnd";

    static final String INCLUDE_REGEXP_KEY = "includeRegexp";
    static final String EXCLUDE_REGEXP_KEY = "excludeRegexp";

    static final String REPLACE_KEY = "replace";

    private TextContentExtractor() {
    }

    static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add(SURROUNDED_BY_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "markers to use to extract portion of a snippet",
                        "\"example-of-transaction\" or [\"example-of-creation\", \"example-of-consumption\"]")
                .add(SURROUNDED_BY_SEPARATOR_KEY, PluginParamType.LIST_OR_SINGLE_STRING_WITH_NULLS,
                        "separator(s) to use for multiple surrounded by blocks",
                        "\"...\" or [\"\", \"...\"]")
                .add(START_LINE_KEY, PluginParamType.STRING,
                        "partial match of start line for snippet extraction", "\"class\"")
                .add(END_LINE_KEY, PluginParamType.STRING,
                        "partial match of end line for snippet extraction", "\"class\"")
                .add(NUMBER_OF_LINES_KEY, PluginParamType.NUMBER,
                        "number of lines to extract given start line", "10")
                .add(EXCLUDE_START_END_KEY, PluginParamType.BOOLEAN,
                        "exclude start and end line for snippet extraction", "true")
                .add(INCLUDE_REGEXP_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "include only lines matching provided regexp(s)", "\"import\" or [\"class R*Base\", \"import B\"")
                .add(EXCLUDE_REGEXP_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "exclude lines matching provided regexp(s)", "\"// marker\" or [\"// marker1\", \"// marker2\"")
                .add(REPLACE_KEY, PluginParamType.LIST_OF_ANY,
                        "replaces values in the resulting snippet",
                        "[\"old-value\", \"new-value\"] or [[\"old-value1\", \"new-value1\"], [\"old-value2\", \"new-value2\"]]");
    }

    public static String extractText(String contentId, String content, PluginParamsOpts opts) {
        if (opts.isEmpty()) {
            return content;
        }

        Text text = new Text(contentId, content);
        Text surroundedBy = cropSurroundedBy(contentId, text, opts);
        Text croppedAtStart = cropStart(surroundedBy, opts);
        Text croppedAtEnd = cropEnd(croppedAtStart, opts);

        Text replacedAll = replaceAll(croppedAtEnd, opts);

        Text withExcludedStartEnd = excludeStartEnd(replacedAll, opts);
        Text withIncludeRegexp = includeRegexp(withExcludedStartEnd, opts);
        Text withExcludedRegexp = excludeRegexp(withIncludeRegexp, opts);

        return withExcludedRegexp.stripIndentation().toString();
    }

    @SuppressWarnings("unchecked")
    private static Text replaceAll(Text text, PluginParamsOpts opts) {
        List<?> fromToOrListOfFromTo = opts.getList(REPLACE_KEY);
        if (fromToOrListOfFromTo.isEmpty()) {
            return text;
        }

        List<List<String>> replacePairsList = new ArrayList<>();
        if (fromToOrListOfFromTo.get(0) instanceof List) {
            replacePairsList.addAll((Collection<? extends List<String>>) fromToOrListOfFromTo);
        } else {
            replacePairsList.add((List<String>) fromToOrListOfFromTo);
        }

        Text result = text;
        for (List<String> fromTo : replacePairsList) {
            if (fromTo.size() != 2) {
                throw new IllegalArgumentException("replace expects list with two values [from, to] or a " +
                        "list of pairs [[from1, to1], [from2, to2]]");
            }

            result = result.replaceAll(fromTo.get(0), fromTo.get(1));
        }

        return result;
    }

    private static Text cropSurroundedBy(String contentId, Text text, PluginParamsOpts opts) {
        List<String> surroundedBy = opts.getList(SURROUNDED_BY_KEY);
        if (surroundedBy.isEmpty()) {
            return text;
        }

        List<String> surroundedBySeparator = opts.getList(SURROUNDED_BY_SEPARATOR_KEY);
        Iterator<String> separatorIt = surroundedBySeparator.iterator();
        String separator = separatorIt.hasNext() ? separatorIt.next() : null;

        Text result = new Text(contentId, "");

        int idx = 0;
        for (String marker : surroundedBy) {
            boolean isLast = idx == (surroundedBy.size() - 1);

            Text surroundedCrop = text.startingWithLineContaining(marker);
            surroundedCrop = surroundedCrop.limitToLineContaining(marker)
                    .cropOneLineFromStartAndEnd();

            result = result.append(surroundedCrop.stripIndentation());
            if (!isLast && separator != null) {
                result = result.append(separator);
            }

            separator = separatorIt.hasNext() ? separatorIt.next() : separator;

            idx++;
        }

        return result;
    }

    private static Text cropStart(Text text, PluginParamsOpts opts) {
        String startLine = opts.get(START_LINE_KEY);
        if (startLine == null) {
            return text;
        }

        return text.startingWithLineContaining(startLine);
    }

    private static Text cropEnd(Text text, PluginParamsOpts opts) {
        Number numberOfLines = opts.get(NUMBER_OF_LINES_KEY);
        if (numberOfLines != null) {
            return text.limitTo(numberOfLines);
        }

        String endLine = opts.get(END_LINE_KEY);
        if (endLine != null) {
            return text.limitToLineContaining(endLine);
        }

        return text;
    }

    private static Text excludeStartEnd(Text text, PluginParamsOpts opts) {
        Boolean exclude = opts.get(EXCLUDE_START_END_KEY, false);
        if (!exclude) {
            return text;
        }

        boolean hasStartLine = opts.has(START_LINE_KEY);
        boolean hasEndLine = opts.has(END_LINE_KEY);
        if ((hasStartLine && hasEndLine) || (!hasStartLine && !hasEndLine)) {
            return text.cropOneLineFromStartAndEnd();
        }

        if (hasStartLine) {
            return text.cropOneLineFromStart();
        }

        return text.cropOneLineFromEnd();
    }

    private static Text includeRegexp(Text text, PluginParamsOpts opts) {
        List<String> includeRegexps = opts.getList(INCLUDE_REGEXP_KEY);
        if (includeRegexps.isEmpty()) {
            return text;
        }

        return text.includeRegexp(createListOfPatterns(includeRegexps));
    }

    private static Text excludeRegexp(Text text, PluginParamsOpts opts) {
        List<String> excludeRegexps = opts.getList(EXCLUDE_REGEXP_KEY);
        if (excludeRegexps.isEmpty()) {
            return text;
        }

        return text.excludeRegexp(createListOfPatterns(excludeRegexps));
    }

    private static List<Pattern> createListOfPatterns(List<String> regexps) {
        return regexps.stream().map(Pattern::compile).collect(toList());
    }

    private static class Text {
        private final String contentId;
        private final List<String> lines;
        private final boolean hasCroppedStart;

        public Text(String contentId, String text) {
            this(contentId, Arrays.asList(text.split("\n")), false);
        }

        public Text(String contentId, List<String> lines, boolean hasCroppedStart) {
            this.contentId = contentId;
            this.lines = lines;
            this.hasCroppedStart = hasCroppedStart;
        }

        Text stripIndentation() {
            return new Text(contentId, StringUtils.stripIndentation(toString()));
        }

        Text append(Text another) {
            List<String> newLines = new ArrayList<>(lines);
            newLines.addAll(another.lines);

            return newText(newLines);
        }

        Text replaceAll(String from, String to) {
            List<String> replaced = lines.stream().map(line -> line.replaceAll(from, to)).collect(toList());
            if (replaced.equals(lines)) {
                throw new IllegalArgumentException("content was not modified using replace from: <" +
                        from + "> to: <" + to + ">");
            }

            return newText(replaced);
        }

        Text append(String line) {
            List<String> newLines = new ArrayList<>(lines);
            newLines.add(line);

            return newText(newLines);
        }

        Text startingWithLineContaining(String subLine) {
            int lineIdx = findLineIdxContaining(subLine);
            return newText(lines.subList(lineIdx, lines.size()), true);
        }

        Text limitToLineContaining(String subLine) {
            int lineIdx = findLineIdxContaining(subLine);
            return newText(lines.subList(0, lineIdx + 1));
        }

        Text limitTo(Number numberOfLines) {
            return newText(lines.subList(0, numberOfLines.intValue()));
        }

        Text cropOneLineFromStartAndEnd() {
            return newText(lines.subList(1, lines.size() - 1));
        }

        Text cropOneLineFromStart() {
            return newText(lines.subList(1, lines.size()));
        }

        Text cropOneLineFromEnd() {
            return newText(lines.subList(0, lines.size() - 1));
        }

        Text includeRegexp(List<Pattern> regexps) {
            List<String> newLines = lines.stream()
                    .filter(line -> regexps.stream().anyMatch(r -> r.matcher(line).find()))
                    .collect(toList());

            if (newLines.isEmpty()) {
                throw new IllegalArgumentException("there are no lines matching includeRegexp " +
                        renderListOfRegexp(regexps) + renderInContent());
            }
            return newText(newLines);
        }

        Text excludeRegexp(List<Pattern> regexps) {
            List<String> newLines = lines.stream()
                    .filter(line -> regexps.stream().noneMatch(r -> r.matcher(line).find()))
                    .collect(toList());

            if (newLines.size() == lines.size()) {
                throw new IllegalArgumentException("there are no lines matching excludeRegexp " +
                        renderListOfRegexp(regexps) + renderInContent());
            }

            return newText(newLines);
        }

        private Text newText(List<String> lines) {
            return new Text(contentId, lines, false);
        }

        private Text newText(List<String> lines, boolean hasCroppedStart) {
            return new Text(contentId, lines, hasCroppedStart);
        }

        private int findLineIdxContaining(String subLine) {
            for (int i = hasCroppedStart ? 1 : 0; i < lines.size(); i++) {
                if (lines.get(i).contains(subLine)) {
                    return i;
                }
            }

            throw new IllegalArgumentException("there is no line containing \"" + subLine + "\"" + renderInContent());
        }

        @Override
        public String toString() {
            return String.join("\n", lines);
        }

        private String renderInContent() {
            return " in <" + contentId + ">:\n" + this;
        }

        private String renderListOfRegexp(List<Pattern> regexps) {
            return regexps.stream().map(p -> "<" + p.toString() + ">")
                    .collect(Collectors.joining(", "));
        }
    }
}
