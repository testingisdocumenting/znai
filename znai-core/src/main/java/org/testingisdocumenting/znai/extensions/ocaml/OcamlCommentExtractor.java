package org.testingisdocumenting.znai.extensions.ocaml;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.utils.RegexpUtils;
import org.testingisdocumenting.znai.utils.StringUtils;

import java.nio.file.Path;
import java.util.regex.Pattern;

class OcamlCommentExtractor {
    private final String[] lines;
    private final String content;

    OcamlCommentExtractor(String content) {
        this.content = content;
        this.lines = content.split("\n");
    }

    String extractCommentBlock(String textToMatch) {
        int idx = findIdxWithMatch(textToMatch);
        int startBlockIdx = startCommentBlockIdx(idx);
        int endBlockIdx = endCommentBlockIdx(startBlockIdx);

        return removeCommentPrefixAndSuffix(extractBlock(startBlockIdx, endBlockIdx));
    }

    /**
     * Converts an OCaml comment block to a list of DocElements.
     * Handles OCaml-specific doc syntax like `[code]` and `{[ multi-line code ]}`.
     *
     * @param componentsRegistry registry containing the markdown parser
     * @param filePath path to the OCaml file (for parser context)
     * @param textToMatch text to find within a comment block
     * @return list of DocElements representing the parsed comment
     */
    MarkupParserResult extractCommentBlockAsDocElements(ComponentsRegistry componentsRegistry, Path filePath, String textToMatch) {
        String commentText = extractCommentBlock(textToMatch);
        String processedText = processOcamlDocSyntax(commentText);

        MarkupParser parser = componentsRegistry.defaultParser();
        return parser.parse(filePath, processedText);
    }

    private String removeCommentPrefixAndSuffix(String commentBlock) {
        String trimmed = commentBlock.trim();

        // Remove opening delimiter - either (** or (*
        int startIndex = trimmed.startsWith("(**") ? 3 : 2;

        // Remove closing delimiter - always *)
        String withoutDelimiters = trimmed.substring(startIndex, trimmed.length() - 2);

        // Check if first line has content (text on same line as opening delimiter)
        String[] lines = withoutDelimiters.split("\n", 2);
        boolean firstLineHasContent = lines.length > 0 && !lines[0].trim().isEmpty();

        if (firstLineHasContent) {
            // First line has text right after delimiter - skip it when calculating indentation
            // This removes both source code indentation and comment alignment indentation
            return StringUtils.stripIndentationSkipFirstLine(withoutDelimiters);
        } else {
            // First line is empty - content starts on next line, use regular strip
            return StringUtils.stripIndentation(withoutDelimiters);
        }
    }

    private String extractBlock(int startBlockIdx, int endBlockIdx) {
        StringBuilder result = new StringBuilder();
        for (int i = startBlockIdx; i <= endBlockIdx; i++) {
            result.append(lines[i]);
            if (i < endBlockIdx) {
                result.append("\n");
            }
        }

        return result.toString();
    }

    private int startCommentBlockIdx(int idx) {
        for (; idx >= 0; idx--) {
            if (lines[idx].contains("(*")) {
                return idx;
            }
        }

        throw new IllegalArgumentException("can't find comment block start, starting idx: " + idx + contentPartForException());
    }

    private int endCommentBlockIdx(int idx) {
        for (; idx < lines.length; idx++) {
            if (lines[idx].contains("*)")) {
                return idx;
            }
        }

        throw new IllegalArgumentException("can't find comment block end, starting idx: " + idx + contentPartForException());
    }

    int findIdxWithMatch(String textToMatch) {
        for (int idx = 0; idx < lines.length; idx++) {
            if (lines[idx].contains(textToMatch)) {
                return idx;
            }
        }

        throw new IllegalArgumentException("can't find text: " + textToMatch + contentPartForException());
    }

    private String contentPartForException() {
        return ", content:\n" + content;
    }

    /**
     * Processes OCaml-specific documentation syntax and converts it to markdown.
     * Converts:
     * - `[code]` to `code`
     * - `{[ multi-line code ]}` to ``` code blocks
     */
    String processOcamlDocSyntax(String text) {
        Pattern multiLineCodePattern = Pattern.compile("\\{\\[([^}]*?)]}", Pattern.DOTALL);
        String afterMultiLine = RegexpUtils.replaceAll(text, multiLineCodePattern, 
            matcher -> {
                String codeContent = matcher.group(1);
                // Remove leading and trailing newlines but preserve internal spacing
                codeContent = codeContent.replaceAll("^\\s*\\n", "").replaceAll("\\n\\s*$", "");
                codeContent = StringUtils.stripIndentation(codeContent);
                return "\n```\n" + codeContent + "\n```";
            });

        // Then handle inline code: [code]
        // But avoid processing content inside code blocks (between ``` markers)
        String[] parts = afterMultiLine.split("```", -1);
        StringBuilder finalResult = new StringBuilder();
        
        Pattern inlineCodePattern = Pattern.compile("\\[([^]]+?)]");

        // this is quite hacky, be sure to change the approach when more OCaml doc elements
        // need to be handled
        for (int i = 0; i < parts.length; i++) {
            if (i % 2 == 0) {
                // Outside code block - process inline code
                final String currentPart = parts[i];
                String part = RegexpUtils.replaceAll(currentPart, inlineCodePattern, matcher -> {
                    String code = matcher.group(1);
                    return "`" + code + "`";
                });
                finalResult.append(part);
            } else {
                // Inside code block - don't process
                finalResult.append(parts[i]);
            }

            if (i < parts.length - 1) {
                finalResult.append("```");
            }
        }

        return finalResult.toString();
    }
}
