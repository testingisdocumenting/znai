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

import com.twosigma.znai.cpp.ClassDefBuilder;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import java.util.ArrayList;
import java.util.List;

class ObjectsDefinitionTokensProcessor {
    private List<String> lines;
    private ArrayList<EntryDef> classDefs;

    private enum ParseState {
        LOOKING_FOR_OBJECT,
        LOOKING_FOR_IDENTIFIER,
        LOOKING_FOR_SCOPE_OPEN,
        LOOKING_FOR_SCOPE_CLOSE,
    }

    private ParseState parseState;
    private ClassDefBuilder currentClassDefBuilder;

    public ObjectsDefinitionTokensProcessor(List<String> lines) {
        this.lines = lines;
        this.parseState = ParseState.LOOKING_FOR_OBJECT;

    }

    public List<EntryDef> process(CPP14Parser parser) {
        this.classDefs = new ArrayList<>();

        TokenStream stream = parser.getTokenStream();
        for (int i = 0; i < stream.size(); i++) {
            Token token = stream.get(i);
            processToken(token);
        }

        return classDefs;
    }

    private boolean processToken(Token token) {
        if (token.getType() == -1) {
            return false;
        }

        switch (parseState) {
            case LOOKING_FOR_OBJECT:
                return lookingForObject(token);
            case LOOKING_FOR_IDENTIFIER:
                return lookingForIdentifier(token);
            case LOOKING_FOR_SCOPE_OPEN:
                return lookingForScopeOpen(token);
            case LOOKING_FOR_SCOPE_CLOSE:
                return lookingForScopeClose(token);
        }

        return false;
    }

    private boolean lookingForScopeOpen(Token token) {
        if (!isScopeOpen(token)) {
            return false;
        }

        currentClassDefBuilder.scopeOpen();
        return changeState(ParseState.LOOKING_FOR_SCOPE_CLOSE);

    }

    private boolean lookingForScopeClose(Token token) {
        currentClassDefBuilder.addToBody(token.getText());

        if (isScopeOpen(token)) {
            currentClassDefBuilder.scopeOpen();
        } else if (isScopeClose(token)) {
            currentClassDefBuilder.scopeClose();

            if (currentClassDefBuilder.isMainScopeClosed()) {
                currentClassDefBuilder.setEndLine(token.getLine());

                classDefs.add(currentClassDefBuilder.build());

                return changeState(ParseState.LOOKING_FOR_OBJECT);
            }
        }

        return false;
    }

    private boolean lookingForObject(Token token) {
        if (!isClassEnumOrStruct(token)) {
            return false;
        }

        currentClassDefBuilder = new ClassDefBuilder(lines);
        currentClassDefBuilder.setStartLine(token.getLine());

        return changeState(ParseState.LOOKING_FOR_IDENTIFIER);
    }

    private boolean lookingForIdentifier(Token token) {
        if (!isIdentifier(token)) {
            return false;
        }

        currentClassDefBuilder.setName(token.getText());
        return changeState(ParseState.LOOKING_FOR_SCOPE_OPEN);
    }

    private boolean changeState(ParseState newState) {
        parseState = newState;
        return true;
    }

    private boolean isScopeOpen(Token token) {
        return token.getType() == 82;
    }

    private boolean isScopeClose(Token token) {
        return token.getType() == 83;
    }

    private boolean isIdentifier(Token token) {
        return token.getType() == 125;
    }

    private boolean isClassEnumOrStruct(Token token) {
        return isClass(token) || isEnum(token) || isStruct(token);
    }

    private boolean isClass(Token token) {
        return token.getType() == 14;
    }

    private boolean isEnum(Token token) {
        return token.getType() == 26;
    }

    private boolean isStruct(Token token) {
        return token.getType() == 59;
    }
}
