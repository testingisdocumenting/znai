package com.twosigma.documentation.cpp.parser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mykola
 */
public class SplitOnCommentsTokensProcessor {
    private final String[] lines;
    private String code;
    private CPP14Parser parser;
    private List<CodePart> parts;
    private String currentSpaces = "";

    public SplitOnCommentsTokensProcessor(String code, CPP14Parser parser) {
        this.code = code;
        this.lines = code.split("\n");
        this.parser = parser;
        this.parts = new ArrayList<>();
    }

    public List<CodePart> extractParts() {
        TokenStream stream = parser.getTokenStream();
        for (int i = 0; i < stream.size(); i++) {
            Token token = stream.get(i);
            processToken(token);
        }

        return parts;
    }

    private void processToken(Token token) {
        if (token.getType() == -1) {
            return;
        }

        CodePart last = parts.isEmpty() ? null : parts.get(parts.size() - 1);

        if (isWhiteSpace(token)) {
            currentSpaces += token.getText();
        } else if (last != null && isCompatibleType(last, token)) {
            last.add(extractText(token));
        } else {
            parts.add(new CodePart(isComment(token), extractText(token)));
        }
    }

    private String extractText(Token token) {
        String result = currentSpaces + token.getText();
        currentSpaces = "";

        return result;
    }

    private String indentationForLine(Token token) {
        String text = lines[token.getLine() - 1].substring(0, token.getCharPositionInLine());
        return text.trim().isEmpty() ? text : "";
    }

    private boolean isCompatibleType(CodePart last, Token token) {
        return isNewLine(token) ||
                (last.isComment() && isComment(token)) ||
                (!last.isComment() && !isComment(token));
    }

    private boolean isComment(Token token) {
        return token.getType() == 141 || token.getType() == 142;
    }

    private boolean isNewLine(Token token) {
        return token.getType() == 140;
    }

    private boolean isWhiteSpace(Token token) {
        return token.getType() == 139;
    }
}
