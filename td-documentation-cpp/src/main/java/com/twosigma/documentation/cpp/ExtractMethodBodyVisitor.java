package com.twosigma.documentation.cpp;

import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.twosigma.documentation.cpp.CodeSnippetsUtils.stripIndentation;

/**
 * @author mykola
 */
public class ExtractMethodBodyVisitor extends CPP14BaseVisitor {
    private final List<String> lines;
    private String code;
    private List<Method> methods;

    public ExtractMethodBodyVisitor(String code) {
        this.code = code;
        this.lines = Arrays.asList(code.replace("\r", "").split("\n"));
        this.methods = new ArrayList<>();
    }

    public List<Method> getMethods() {
        return methods;
    }

    @Override
    public Object visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        String methodName = textBeforeParenthesis(ctx.declarator().getText());

        Method method = new Method(methodName,
                codeContent(ctx.getStart(), ctx.getStop()),
                stripIndentation(removeBrackets(ctx.functionbody().getStart(), ctx.functionbody().getStop())));
        methods.add(method);

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
