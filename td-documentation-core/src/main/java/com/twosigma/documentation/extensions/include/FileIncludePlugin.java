package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginParamsOpts;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.docelement.DocElementType;
import com.twosigma.documentation.search.SearchScore;
import com.twosigma.documentation.search.SearchText;
import com.twosigma.utils.StringUtils;

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
public class FileIncludePlugin implements IncludePlugin {
    private String fileName;
    private String text;

    @Override
    public String id() {
        return "file";
    }

    @Override
    public IncludePlugin create() {
        return new FileIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        fileName = pluginParams.getFreeParam();

        text = extractText(componentsRegistry.resourceResolver().
                textContent(fileName), pluginParams.getOpts());

        String providedLang = pluginParams.getOpts().getString("lang");
        String langToUse = (providedLang == null) ? langFromFileName(fileName) : providedLang;

        Map<String, Object> props = CodeSnippetsProps.create(langToUse, text);
        props.putAll(pluginParams.getOpts().toMap());

        return PluginResult.docElement(DocElementType.SNIPPET, props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(
                componentsRegistry.resourceResolver().fullPath(fileName)));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(text);
    }

    private String extractText(String fileContent, PluginParamsOpts opts) {
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

    private Text cropStart(Text text, PluginParamsOpts opts) {
        String startLine = opts.get("startLine");
        if (startLine == null) {
            return text;
        }

        return text.startingWithLineContaining(startLine);
    }

    private Text cropEnd(Text text, PluginParamsOpts opts) {
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

    private Text exclude(Text text, PluginParamsOpts opts) {
        Boolean exclude = opts.get("exclude", false);
        if (!exclude) {
            return text;
        }

        return text.cropOneLineFromStartAndEnd();
    }

    private Text includeRegexp(Text text, PluginParamsOpts opts) {
        String includeRegexp = opts.get("includeRegexp");
        if (includeRegexp == null) {
            return text;
        }

        return text.includeRegexp(Pattern.compile(includeRegexp));
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
