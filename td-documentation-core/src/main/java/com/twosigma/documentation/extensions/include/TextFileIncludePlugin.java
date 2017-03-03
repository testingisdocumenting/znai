package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class TextFileIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "file";
    }

    @Override
    public void reset(IncludeContext context) {
    }

    @Override
    public IncludePluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        String fileName = includeParams.getFreeParam();

        String text = extractText(componentsRegistry.includeResourceResolver().
                textContent(fileName), includeParams.getOpts());

        String providedLang = includeParams.getOpts().getString("lang");
        String langToUse = (providedLang == null) ? langFromFileName(fileName) : providedLang;

        Map<String, Object> props = CodeSnippetsProps.create(componentsRegistry.codeTokenizer(), langToUse, text);
        props.putAll(includeParams.getOpts().toMap());

        return IncludePluginResult.reactComponent(DocElementType.SNIPPET, props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry, IncludeParams includeParams) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.includeResourceResolver().fullPath(includeParams.getFreeParam())));
    }

    private String extractText(String text, IncludeParamsOpts opts) {
        if (opts.isEmpty()) {
            return text;
        }

        Number numberOfLines = opts.has("numberOfLines") ? opts.get("numberOfLines") : null;
        if (opts.has("startLine")) {
            String startLine = opts.get("startLine").toString();
            return new Text(text).startingWithLineMatching(startLine, numberOfLines).toString();
        }

        return text;
    }

    private static String langFromFileName(String fileName) {
        String ext = extFromFileName(fileName);
        switch (ext) {
            case "js": return "javascript";
            default: return ext;
        }
    }

    private static String extFromFileName(String fileName) {
        int dotLastIdx = fileName.lastIndexOf('.');
        if (dotLastIdx == -1) {
            return "";
        }

        return fileName.substring(dotLastIdx + 1);
    }

    private static class Text {
        private final List<String> lines;

        public Text(String text) {
            this.lines = Arrays.asList(text.split("\n"));
        }

        public Text(List<String> lines) {
            this.lines = lines;
        }

        int findLineIdxContaining(String regex) {
            Pattern pattern = Pattern.compile(regex);
            for (int i = 0; i < lines.size(); i++) {
                if (pattern.matcher(lines.get(i)).find()) {
                    return i;
                }
            }

            return -1;
        }

        @Override
        public String toString() {
            return lines.stream().collect(Collectors.joining("\n"));
        }

        Text startingWithLineMatching(String pattern, Number numberOfLines) {
            int lineIdx = findLineIdxContaining(pattern);
            if (lineIdx == -1) {
                throw new IllegalArgumentException("<there is no line matching " + pattern + " in:\n" + toString());
            }

            return new Text(lines.subList(lineIdx, numberOfLines != null  ? lineIdx + numberOfLines.intValue() : lines.size()));
        }
    }
}
