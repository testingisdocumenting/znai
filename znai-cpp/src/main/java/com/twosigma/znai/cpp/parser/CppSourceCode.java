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

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CppSourceCode {
    private CppSourceCode() {
    }

    public static String entryDefinition(String code, String methodName) {
        Optional<EntryDef> first = findDefinition(code, methodName);
        return first.map(EntryDef::getFull).orElse("");
    }

    public static String entryBodyOnly(String code, String methodName) {
        Optional<EntryDef> first = findDefinition(code, methodName);
        return first.map(EntryDef::getBodyOnly).orElse("");
    }

    public static List<CodePart> splitOnComments(String code) {
        CPP14Parser parser = createParser(code);
        parser.translationunit().accept(new CPP14BaseVisitor());

        SplitOnCommentsTokensProcessor processor = new SplitOnCommentsTokensProcessor(parser);
        return processor.extractParts();
    }

    public static List<String> topLevelComments(String code) {
        return Collections.emptyList();
    }

    private static Optional<EntryDef> findDefinition(String code, String methodName) {
        ExtractBodyVisitor visitor = parse(code);
        return visitor.getEntries().filter(m -> methodName.equals(m.getName())).findFirst();
    }

    private static ExtractBodyVisitor parse(String code) {
        CPP14Parser parser = createParser(code);
        ExtractBodyVisitor visitor = new ExtractBodyVisitor(parser, code);
        parser.translationunit().accept(visitor);

        return visitor;
    }

    private static CPP14Parser createParser(String code) {
        CPP14Lexer lexer = new CPP14Lexer(new ANTLRInputStream(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        return new CPP14Parser(tokens);
    }
}
