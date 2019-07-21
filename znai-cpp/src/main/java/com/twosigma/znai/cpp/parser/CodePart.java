/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
