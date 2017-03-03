package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    private String extractText(String fileContent, IncludeParamsOpts opts) {
        if (opts.isEmpty()) {
            return fileContent;
        }

        Text text = new Text(fileContent);
        Text croppedAtStart = cropStart(text, opts);
        Text croppedAtEnd = cropEnd(croppedAtStart, opts);

        Boolean exclude = opts.get("exclude");
        Text withExcludedLines = exclude != null && exclude ?
                croppedAtEnd.cropOneLineFromStartAndEnd() : croppedAtEnd;

        return withExcludedLines.toString();
    }

    private Text cropStart(Text text, IncludeParamsOpts opts) {
        String startLine = opts.get("startLine");
        if (startLine != null) {
            return text.startingWithLineContaining(startLine);
        }

        return text;
    }

    private Text cropEnd(Text text, IncludeParamsOpts opts) {
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
            return lines.stream().collect(Collectors.joining("\n"));
        }
    }
}
