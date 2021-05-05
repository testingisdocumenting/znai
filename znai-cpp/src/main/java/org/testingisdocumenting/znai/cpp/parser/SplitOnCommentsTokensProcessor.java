/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.cpp.parser;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import java.util.ArrayList;
import java.util.List;

public class SplitOnCommentsTokensProcessor {
    private final CPP14Parser parser;
    private final List<CodePart> parts;
    private String currentSpaces = "";

    public SplitOnCommentsTokensProcessor(CPP14Parser parser) {
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
