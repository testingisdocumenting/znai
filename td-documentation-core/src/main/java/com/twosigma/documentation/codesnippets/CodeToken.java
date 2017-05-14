package com.twosigma.documentation.codesnippets;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author mykola
 */
public class CodeToken {
    private String type;
    private String singleContent;
    private List<CodeToken> content;

    public CodeToken(String type, String content) {
        this.type = type;
        this.singleContent = content;
    }

    public CodeToken(String type, List<CodeToken> content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public List<CodeToken> getContent() {
        return content;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("content", singleContent == null ?
                singleContent :
                content.stream().map(CodeToken::toMap).collect(toList()));

        return result;
    }
}
