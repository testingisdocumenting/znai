package com.twosigma.documentation.extensions.file;

import com.twosigma.documentation.extensions.PluginParamsOpts;
import com.twosigma.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class FilePlugin {
    private FilePlugin() {
    }

    public static String extractText(String fileContent, PluginParamsOpts opts) {
        if (opts.isEmpty()) {
            return fileContent;
        }

        Text text = new Text(fileContent);
        Text croppedAtStart = cropStart(text, opts);
        Text croppedAtEnd = cropEnd(croppedAtStart, opts);

        Text withExcludedLines = exclude(croppedAtEnd, opts);
        Text withIncludeRegexp = includeRegexp(withExcludedLines, opts);

        return StringUtils.stripIndentation(withIncludeRegexp.toString());
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

    private static Text exclude(Text text, PluginParamsOpts opts) {
        Boolean exclude = opts.get("exclude", false);
        if (!exclude) {
            return text;
        }

        return text.cropOneLineFromStartAndEnd();
    }

    private static Text includeRegexp(Text text, PluginParamsOpts opts) {
        String includeRegexp = opts.get("includeRegexp");
        if (includeRegexp == null) {
            return text;
        }

        return text.includeRegexp(Pattern.compile(includeRegexp));
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

        Text includeRegexp(Pattern regexp) {
            return new Text(lines.stream().filter(l -> regexp.matcher(l).find()).collect(Collectors.toList()));
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
