package com.twosigma.documentation.cpp.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;
import java.util.Optional;

/**
 * @author mykola
 */
public class CppSourceCode {
    private CppSourceCode() {
    }

    public static String methodBody(String code, String methodName) {
        Optional<Method> first = findMethod(code, methodName);
        return first.map(Method::getBodyWithDecl).orElse("");
    }

    public static String methodBodyOnly(String code, String methodName) {
        Optional<Method> first = findMethod(code, methodName);
        return first.map(Method::getBodyOnly).orElse("");
    }

    public static List<CodePart> splitOnComments(String code) {
        CPP14Parser parser = createParser(code);
        parser.translationunit().accept(new CPP14BaseVisitor());

        SplitOnCommentsTokensProcessor processor = new SplitOnCommentsTokensProcessor(code, parser);
        List<CodePart> parts = processor.extractParts();
        parts.forEach(System.out::println);

        return parts;
    }

    private static Optional<Method> findMethod(String code, String methodName) {
        CPP14Parser parser = createParser(code);
        ExtractMethodBodyVisitor visitor = new ExtractMethodBodyVisitor(code);
        parser.translationunit().accept(visitor);

        return visitor.getMethods().stream().filter(m -> methodName.equals(m.getName())).findFirst();
    }

    private static CPP14Parser createParser(String code) {
        CPP14Lexer lexer = new CPP14Lexer(new ANTLRInputStream(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        return new CPP14Parser(tokens);
    }
}
