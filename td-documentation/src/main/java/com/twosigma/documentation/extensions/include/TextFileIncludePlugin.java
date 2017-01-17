package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.extensions.ReactComponent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mykola
 */
public class TextFileIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "text-file";
    }

    @Override
    public void reset(IncludeContext context) {
    }

    @Override
    public ReactComponent process(IncludeResourcesResolver resourcesResolver, IncludeParams includeParams) {
        String text = extractText(resourcesResolver.textContent(includeParams.getFreeParam()), includeParams.getOpts());

        Map<String, Object> props = new LinkedHashMap<>(includeParams.getOpts());
        props.put("text", text);

        return new ReactComponent("FileTextContent", props);
    }

    private String extractText(String text, Map<String, ?> opts) {
        if (opts.isEmpty()) {
            return text;
        }

        Number numberOfLines = opts.containsKey("numberOfLines") ? (Number) opts.get("numberOfLines") : Integer.MAX_VALUE;
        if (opts.containsKey("startLine")) {
            String startLine = opts.get("startLine").toString();
            return new Text(text).startingWithLineContaining(startLine, numberOfLines).toString();
        }

        return text;
    }

    @Override
    public String textForSearch() {
        return null;
    }

    private static class Text {
        private final String text;
        private final String[] lines;

        public Text(String text) {
            this.text = text;
            this.lines = text.split("\n");
        }

        int findLineIdxContaining(String subLine) {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains(subLine)) {
                    return i;
                }
            }

            return -1;
        }

        @Override
        public String toString() {
            return text;
        }

        Text startingWithLineContaining(String subline, Number numberOfLines) {
            int lineIdx = findLineIdxContaining(subline);
            if (lineIdx == -1) {
                throw new IllegalArgumentException("<there is no line containing '" + subline + "'> in:\n" + text);
            }

            return new Text(Arrays.stream(lines).skip(lineIdx).limit(numberOfLines.intValue()).collect(Collectors.joining("\n")));
        }
    }
}
