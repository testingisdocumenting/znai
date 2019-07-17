package com.twosigma.znai.cpp.parser;

import com.twosigma.utils.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CodePart {
    private boolean isComment;
    private StringBuilder data;

    public CodePart(boolean isComment, String data) {
        this.isComment = isComment;
        this.data = new StringBuilder();
        add(data);
    }

    public boolean isComment() {
        return isComment;
    }

    public boolean isEmpty() {
        return data.toString().trim().isEmpty();
    }

    public void add(String part) {
        data.append(isComment ? removeCommentChars(part) : part);
    }

    private String removeCommentChars(String part) {
        return Arrays.stream(part.split("\n")).map(this::removeCommentCharsFromLine)
                .collect(Collectors.joining("\n"));
    }

    private String removeCommentCharsFromLine(String line) {
        String trimmed = line.trim();
        return trimmed.replaceFirst("^//*", "")
                .replaceFirst("^\\*", "")
                .replaceFirst("^//*", "");
    }

    public String getData() {
        return isComment ?
                data.toString().trim() :
                StringUtils.stripIndentation(data.toString());
    }

    @Override
    public String toString() {
        return "CodePart{" +
                "isComment=" + isComment() +
                ", data=" + getData() +
                '}';
    }
}
