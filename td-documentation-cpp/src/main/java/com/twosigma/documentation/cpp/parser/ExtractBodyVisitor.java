package com.twosigma.documentation.cpp.parser;

import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.twosigma.utils.StringUtils.stripIndentation;

/**
 * @author mykola
 */
public class ExtractBodyVisitor extends CPP14BaseVisitor {
    private final List<String> lines;
    private CPP14Parser parser;
    private String code;
    private List<EntryDef> entries;

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
    public Object visitTranslationunit(CPP14Parser.TranslationunitContext ctx) {
        Object translationunit = super.visitTranslationunit(ctx);

        ObjectsDefinitionTokensProcessor objectsDefinitionTokensProcessor = new ObjectsDefinitionTokensProcessor(lines);
        entries.addAll(objectsDefinitionTokensProcessor.process(parser));

        return translationunit;
    }

    @Override
    public Object visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        String methodName = textBeforeParenthesis(ctx.declarator().getText());

        EntryDef method = new EntryDef(methodName,
                codeContent(ctx.getStart(), ctx.getStop()),
                stripIndentation(removeBrackets(ctx.functionbody().getStart(), ctx.functionbody().getStop())));
        entries.add(method);

        return super.visitFunctiondefinition(ctx);
    }

    private String removeIndentation(String line, Integer indentation) {
        if (line.trim().isEmpty()) {
            return line;
        }

        return line.substring(indentation);
    }

    private boolean notEmptyLine(String s) {
        return ! s.trim().isEmpty();
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
