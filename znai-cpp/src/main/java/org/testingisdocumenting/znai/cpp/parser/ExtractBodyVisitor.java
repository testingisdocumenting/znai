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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.testingisdocumenting.znai.utils.StringUtils.stripIndentation;

public class ExtractBodyVisitor extends CPP14BaseVisitor<Void> {
    private final List<String> lines;
    private final CPP14Parser parser;
    private final String code;
    private final List<EntryDef> entries;

    public ExtractBodyVisitor(CPP14Parser parser, String code) {
        this.parser = parser;
        this.code = code;
        this.lines = Arrays.asList(code.replace("\r", "").split("\n"));
        this.entries = new ArrayList<>();
    }

    public Stream<EntryDef> getEntries() {
        return entries.stream();
    }

    @Override
    public Void visitTranslationunit(CPP14Parser.TranslationunitContext ctx) {
        super.visitTranslationunit(ctx);

        ObjectsDefinitionTokensProcessor objectsDefinitionTokensProcessor = new ObjectsDefinitionTokensProcessor(lines);
        entries.addAll(objectsDefinitionTokensProcessor.process(parser));

        return null;
    }

    @Override
    public Void visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        String methodName = textBeforeParenthesis(ctx.declarator().getText());

        EntryDef method = new EntryDef(methodName,
                codeContent(ctx.getStart(), ctx.getStop()),
                stripIndentation(removeBrackets(ctx.functionbody().getStart(), ctx.functionbody().getStop())));
        entries.add(method);

        return super.visitFunctiondefinition(ctx);
    }

    private String textBeforeParenthesis(String text) {
        int idx = text.indexOf('(');
        return idx == -1 ? text : text.substring(0, idx);
    }

    private String removeBrackets(Token start, Token stop) {
        return code.substring(start.getStartIndex() + 1, stop.getStartIndex());
    }

    private String codeContent(Token start, Token stop) {
        return code.substring(start.getStartIndex(), stop.getStartIndex() + 1);
    }
}
