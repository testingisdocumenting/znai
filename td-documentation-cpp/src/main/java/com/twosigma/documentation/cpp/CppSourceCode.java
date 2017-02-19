package com.twosigma.documentation.cpp;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Optional;

/**
 * @author mykola
 */
public class CppSourceCode {
    private CppSourceCode() {
    }

    public static String methodBody(String code, String methodName) {
        CPP14Parser parser = createParser(code);
        ExtractMethodBodyVisitor visitor = new ExtractMethodBodyVisitor(code);
        parser.translationunit().accept(visitor);

        Optional<Method> first = visitor.getMethods().stream().filter(m -> methodName.equals(m.getName())).findFirst();
        return first.map(Method::getBodyWithDecl).orElse("");
    }

    private static CPP14Parser createParser(String code) {
        CPP14Lexer lexer = new CPP14Lexer(new ANTLRInputStream(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        return new CPP14Parser(tokens);
    }
}
