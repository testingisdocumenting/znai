package org.testingisdocumenting.znai.extensions.ocaml;

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

    private String removeCommentPrefixAndSuffix(String commentBlock) {
        String trimmed = commentBlock.trim();
        
        // Remove opening delimiter - either (** or (*
        int startIndex = trimmed.startsWith("(**") ? 3 : 2;
        
        // Remove closing delimiter - always *)
        String withoutDelimiters = trimmed.substring(startIndex, trimmed.length() - 2);
        
        return withoutDelimiters.trim();
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

    String contentPartForException() {
        return ", content:\n" + content;
    }
}
