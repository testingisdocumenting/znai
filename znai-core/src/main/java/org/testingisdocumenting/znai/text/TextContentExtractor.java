/*
 * Copyright 2023 znai maintainers
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

package org.testingisdocumenting.znai.text;

import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TextContentExtractor {
    static final String SURROUNDED_BY_KEY = "surroundedBy";
    static final String SURROUNDED_BY_SEPARATOR_KEY = "surroundedBySeparator";
    static final String SURROUNDED_BY_KEEP_KEY = "surroundedByKeep";
    static final String SURROUNDED_BY_SCOPE_KEY = "surroundedByScope";

    static final String SURROUNDED_BY_SCOPE_START_SUB_KEY = "start";
    static final String SURROUNDED_BY_SCOPE_SCOPE_SUB_KEY = "scope";

    static final String START_LINE_KEY = "startLine";
    static final String END_LINE_KEY = "endLine";
    static final String NUMBER_OF_LINES_KEY = "numberOfLines";
    static final String EXCLUDE_START_KEY = "excludeStart";
    static final String EXCLUDE_END_KEY = "excludeEnd";
    static final String EXCLUDE_START_END_KEY = "excludeStartEnd";

    static final String INCLUDE_KEY = "include";
    static final String EXCLUDE_KEY = "exclude";

    static final String INCLUDE_REGEXP_KEY = "includeRegexp";
    static final String EXCLUDE_REGEXP_KEY = "excludeRegexp";

    static final String REPLACE_KEY = "replace";

    private TextContentExtractor() {
    }

    public static PluginParamsDefinition createParamsDefinition() {
        return new PluginParamsDefinition()
                .add(SURROUNDED_BY_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "markers to use to extract portion of a snippet",
                        "\"example-of-transaction\" or [\"example-of-creation\", \"example-of-consumption\"]")
                .add(SURROUNDED_BY_SEPARATOR_KEY, PluginParamType.LIST_OR_SINGLE_STRING_WITH_NULLS,
                        "separator(s) to use for multiple surrounded by blocks",
                        "\"...\" or [\"\", \"...\"]")
                .add(SURROUNDED_BY_KEEP_KEY, PluginParamType.BOOLEAN,
                        "keep surrounded by text",
                        "true")
                .add(SURROUNDED_BY_SCOPE_KEY, PluginParamType.OBJECT,
                        "extract text based on start line and scope boundaries like {}",
                        "{start: \"if (myCondition)\", scope: \"{}\"}")
                .add(START_LINE_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "partial match of start line(s) for snippet extraction", "\"class\" or [\"if (conditionA)\", \"nested-sub-line\"]")
                .add(END_LINE_KEY, PluginParamType.STRING,
                        "partial match of end line for snippet extraction", "\"class\"")
                .add(NUMBER_OF_LINES_KEY, PluginParamType.NUMBER,
                        "number of lines to extract given start line", "10")
                .add(EXCLUDE_START_KEY, PluginParamType.BOOLEAN,
                        "exclude start line for snippet extraction", "true")
                .add(EXCLUDE_END_KEY, PluginParamType.BOOLEAN,
                        "exclude end line for snippet extraction", "true")
                .add(EXCLUDE_START_END_KEY, PluginParamType.BOOLEAN,
                        "exclude start and end line for snippet extraction", "true")
                .add(INCLUDE_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "include only lines containing provided text(s)", "\"import\" or [\"class\", \"import\"")
                .add(EXCLUDE_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "exclude lines containing provided text(s)", "\"// marker\" or [\"// marker1\", \"// marker2\"")
                .add(INCLUDE_REGEXP_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "include only lines matching provided regexp(s)", "\"import org.util.*key\" or [\"class R*Base\", \"import B*One\"")
                .add(EXCLUDE_REGEXP_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "exclude lines matching provided regexp(s)", "\"// marker-.*-fin\" or [\"// marker-.*-fin\", \"// another-.*-end\"")
                .add(REPLACE_KEY, PluginParamType.LIST_OF_ANY,
                        "replaces values in the resulting snippet",
                        "[\"old-value\", \"new-value\"] or [[\"old-value1\", \"new-value1\"], [\"old-value2\", \"new-value2\"]]");
    }

    public static String extractText(String contentId, String content, PluginParamsOpts opts) {
        Text text = new Text(contentId, content);

        Text surroundedBy = cropSurroundedBy(contentId, text, opts);
        Text croppedAtStart = cropStart(surroundedBy, opts);
        Text croppedAtEnd = cropEnd(croppedAtStart, opts);
        Text croppedByScope = cropSurroundedByRegion(croppedAtEnd, opts);

        Text replacedAll = replaceAll(croppedByScope, opts);

        Text withExcludedStart = excludeStart(replacedAll, opts);
        Text withExcludedStartEnd = excludeEnd(withExcludedStart, opts);

        Text withIncludeContains = includeContains(withExcludedStartEnd, opts);
        Text withExcludeContains = excludeContains(withIncludeContains, opts);

        Text withIncludeRegexp = includeRegexp(withExcludeContains, opts);
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

        boolean keepMarker = opts.get(SURROUNDED_BY_KEEP_KEY, false);

        List<String> surroundedBySeparator = opts.getList(SURROUNDED_BY_SEPARATOR_KEY);
        Iterator<String> separatorIt = surroundedBySeparator.iterator();
        String separator = separatorIt.hasNext() ? separatorIt.next() : null;

        Text result = new Text(contentId, "");

        int idx = 0;
        for (String marker : surroundedBy) {
            boolean isLast = idx == (surroundedBy.size() - 1);

            Text surroundedCrop = text.startingWithLineContaining(marker);
            surroundedCrop = surroundedCrop.limitToLineContaining(marker, (subLine) -> "there is no second marker \"" + subLine + "\"" + text.renderInContent());
            if (!keepMarker) {
               surroundedCrop = surroundedCrop.cropOneLineFromStartAndEnd();
            }
            surroundedCrop = surroundedCrop.stripIndentation();

            if (surroundedCrop.isEmpty()) {
                throw new RuntimeException("no content present after " + SURROUNDED_BY_KEY + " " + marker);
            }

            result = result.append(surroundedCrop);
            if (!isLast && separator != null) {
                result = result.append(separator);
            }

            separator = separatorIt.hasNext() ? separatorIt.next() : separator;

            idx++;
        }

        return result;
    }

    private static Text cropSurroundedByRegion(Text originalText, PluginParamsOpts opts) {
        Object surroundedByRaw = opts.get(SURROUNDED_BY_SCOPE_KEY);
        if (surroundedByRaw == null) {
            return originalText;
        }

        if (!(surroundedByRaw instanceof Map)) {
            throw new IllegalArgumentException(regionWrongFormatMessage());
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> region = (Map<String, Object>) surroundedByRaw;

        String start = region.getOrDefault(SURROUNDED_BY_SCOPE_START_SUB_KEY, "").toString();
        if (start.isEmpty()) {
            throw new IllegalArgumentException(regionWrongFormatMessage());
        }

        String scope = region.getOrDefault(SURROUNDED_BY_SCOPE_SCOPE_SUB_KEY, "").toString();
        Text croppedFromStart = originalText.startingWithLineContaining(start);
        TextLinesAccessor linesAccessor = TextLinesAccessor.createFromList(croppedFromStart.lines);
        char scopeStart = scope.charAt(0);
        char scopeEnd = scope.charAt(1);
        RegionScopeExtractor extractor = new RegionScopeExtractor(linesAccessor, 0, scope);
        extractor.process();

        int scopeStartLineIdx = extractor.getResultStartLineIdx();
        if (scopeStartLineIdx == -1) {
            throw new IllegalArgumentException("can't find scope start \"" + scopeStart + "\" after line \"" + start + "\"" + originalText.renderInContent());

        }
        int scopeEndLineIdx = extractor.getResultEndLineIdx();

        if (scopeEndLineIdx == -1) {
            throw new IllegalArgumentException("can't find scope end \"" + scopeEnd + "\" after line \"" + start + "\"" + originalText.renderInContent());
        }

        return croppedFromStart.limitToSize(scopeEndLineIdx + 1);
    }

    private static String regionWrongFormatMessage() {
        return SURROUNDED_BY_SCOPE_KEY + " should be in format {" + SURROUNDED_BY_SCOPE_START_SUB_KEY + ": \"line-star\", " +
                SURROUNDED_BY_SCOPE_SCOPE_SUB_KEY + ": \"{}\"}";
    }

    private static Text cropStart(Text text, PluginParamsOpts opts) {
        List<String> startLines = opts.getList(START_LINE_KEY);
        if (startLines.isEmpty()) {
            return text;
        }

        if (startLines.size() == 1) {
            return text.startingWithLineContaining(startLines.get(0));
        }

        int idx = findStartIdxForMultiLines(text, startLines);
        if (idx == -1) {
            throw new IllegalArgumentException("can't find sequence of start lines:\n  " + String.join("\n  ", startLines) + text.renderInContent());
        }

        return text.subList(idx, text.lines.size());
    }

    private static int findStartIdxForMultiLines(Text text, List<String> matchLines) {
        for (int idx = 0; idx < text.lines.size() - matchLines.size(); idx++) {
            if (matchLinesContaining(text, idx, matchLines)) {
                return idx;
            }
        }

        return -1;
    }

    private static boolean matchLinesContaining(Text text, int startIdx, List<String> matchLines) {
        int idx = startIdx;
        int len = text.lines.size();
        int matchLen = matchLines.size();
        if (idx + matchLen > len) {
            return false;
        }

        for (int matchIdx = 0;idx < len && matchIdx < matchLen; idx++, matchIdx++) {
            String line = text.lines.get(idx);
            String matchLine = matchLines.get(matchIdx);

            if (!line.contains(matchLine)) {
                return false;
            }
        }

        return true;
    }

    private static Text cropEnd(Text text, PluginParamsOpts opts) {
        Number numberOfLines = opts.get(NUMBER_OF_LINES_KEY);
        if (numberOfLines != null) {
            return text.limitToSize(numberOfLines);
        }

        String endLine = opts.get(END_LINE_KEY);
        if (endLine != null) {
            return text.limitToLineContaining(endLine, text::defaultNoLineFoundMessage);
        }

        return text;
    }

    private static Text excludeStart(Text text, PluginParamsOpts opts) {
        Boolean excludeStart = opts.get(EXCLUDE_START_KEY, false);
        Boolean excludeStartEnd = opts.get(EXCLUDE_START_END_KEY, false);
        if (!excludeStart && !excludeStartEnd) {
            return text;
        }

        return text.cropOneLineFromStart();
    }

    private static Text excludeEnd(Text text, PluginParamsOpts opts) {
        Boolean excludeEnd = opts.get(EXCLUDE_END_KEY, false);
        Boolean excludeStartEnd = opts.get(EXCLUDE_START_END_KEY, false);
        if (!excludeEnd && !excludeStartEnd) {
            return text;
        }

        return text.cropOneLineFromEnd();
    }

    private static Text includeContains(Text text, PluginParamsOpts opts) {
        List<String> texts = opts.getList(INCLUDE_KEY);
        if (texts.isEmpty()) {
            return text;
        }

        return text.includeContains(texts);
    }

    private static Text excludeContains(Text text, PluginParamsOpts opts) {
        List<String> texts = opts.getList(EXCLUDE_KEY);
        if (texts.isEmpty()) {
            return text;
        }

        return text.excludeContains(texts);
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

        boolean isEmpty() {
            return lines.isEmpty();
        }

        Text stripIndentation() {
            String text = StringUtils.stripIndentation(toString());
            return text.isEmpty() ?
                    new Text(contentId, Collections.emptyList(), false) :
                    new Text(contentId, text);
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
            int lineIdx = findLineIdxContainingThrow(subLine);
            return newText(lines.subList(lineIdx, lines.size()), true);
        }

        Text limitToLineContaining(String subLine, Function<String, String> errorMessageFunc) {
            int lineIdx = findLineIdxContainingThrow(subLine, errorMessageFunc);
            return subList(0, lineIdx + 1);
        }

        Text limitToSize(Number numberOfLines) {
            return subList(0, numberOfLines.intValue());
        }

        Text cropOneLineFromStartAndEnd() {
            return subList(1, lines.size() - 1);
        }

        Text cropOneLineFromStart() {
            return subList(1, lines.size());
        }

        Text cropOneLineFromEnd() {
            return subList(0, lines.size() - 1);
        }

        Text subList(int from, int to) {
            return newText(lines.subList(from, to));
        }

        <E> Text include(List<E> inputs, String matchingLabel, BiFunction<String, E, Boolean> predicate) {
            List<String> newLines = lines.stream()
                    .filter(line -> inputs.stream().anyMatch(input -> predicate.apply(line, input)))
                    .collect(toList());

            if (newLines.isEmpty()) {
                throw new IllegalArgumentException("there are no lines " + matchingLabel + " " +
                        renderListOfStrings(inputs) + renderInContent());
            }
            return newText(newLines);
        }

        Text includeContains(List<String> texts) {
            return include(texts, "containing", String::contains);
        }

        Text includeRegexp(List<Pattern> regexps) {
            return include(regexps, "matching regexp", (line, regexp) -> regexp.matcher(line).find());
        }

        <E> Text exclude(List<E> inputs, String matchingLabel, BiFunction<String, E, Boolean> predicate) {
            List<String> newLines = lines.stream()
                    .filter(line -> inputs.stream().noneMatch(input -> predicate.apply(line, input)))
                    .collect(toList());

            if (newLines.size() == lines.size()) {
                throw new IllegalArgumentException("there are no lines " + matchingLabel + " " +
                        renderListOfStrings(inputs) + renderInContent());
            }

            return newText(newLines);
        }

        Text excludeContains(List<String> texts) {
            return exclude(texts, "containing", String::contains);
        }

        Text excludeRegexp(List<Pattern> regexps) {
            return exclude(regexps, "matching regexp", (line, regexp) -> regexp.matcher(line).find());
        }

        private Text newText(List<String> lines) {
            return new Text(contentId, lines, false);
        }

        private Text newText(List<String> lines, boolean hasCroppedStart) {
            return new Text(contentId, lines, hasCroppedStart);
        }

        private int findLineIdxContainingThrow(String subLine) {
            return findLineIdxContainingThrow(subLine, this::defaultNoLineFoundMessage);
        }

        private int findLineIdxContainingThrow(String subLine, Function<String, String> errorMessageFunc) {
            int idx = findLineIdxContaining(subLine);
            if (idx == -1) {
                throw new IllegalArgumentException(errorMessageFunc.apply(subLine));
            }

            return idx;
        }

        private int findLineIdxContaining(String subLine) {
            for (int idx = hasCroppedStart ? 1 : 0; idx < lines.size(); idx++) {
                if (lines.get(idx).contains(subLine)) {
                    return idx;
                }
            }

            return -1;
        }

        @Override
        public String toString() {
            return String.join("\n", lines);
        }

        private String renderListOfStrings(List<?> list) {
            return list.stream().map(p -> "<" + p.toString() + ">")
                    .collect(Collectors.joining(", "));
        }

        private String defaultNoLineFoundMessage(String subLine) {
            return "there is no line containing \"" + subLine + "\"" + renderInContent();
        }

        private String renderInContent() {
            return " in <" + contentId + ">:\n" + this;
        }
    }
}
