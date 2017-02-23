package com.twosigma.documentation.cpp.parser

import com.twosigma.documentation.cpp.parser.CppSourceCode
import com.twosigma.utils.ResourceUtils
import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class CppSourceCodeTest {
    static String code = ResourceUtils.textContent("test.cpp");

    @Test
    void "extract function definition"() {
        Assert.assertEquals("""int main() {
    int test = 2;

    // comment
    // in two lines
    int b = 3;
    int d = 3;

    /*
     * multi line comment
     * of multi lines text
    */
    int e = 5;
}""", CppSourceCode.methodBody(code, "main"))
    }

    @Test
    void "extract method definition"() {
        Assert.assertEquals("""void ClassName::method_name() {
   b= 2;
}""", CppSourceCode.methodBody(code, "ClassName::method_name"))
    }

    @Test
    void "extract body only and strip extra indentation"() {
        Assert.assertEquals("""int test = 2;

// comment
// in two lines
int b = 3;
int d = 3;

/*
 * multi line comment
 * of multi lines text
*/
int e = 5;""", CppSourceCode.methodBodyOnly(code, "main"))
    }

    @Test
    void "split source code into groups using comments"() {
        def main = CppSourceCode.methodBody(code, "main")
        def parts = CppSourceCode.splitOnComments(main)
        assert parts.size() == 5
        Assert.assertEquals("int main() {\n" +
                "    int test = 2;", parts[0].data)
        Assert.assertEquals("comment in two lines", parts[1].data)
        Assert.assertEquals("int b = 3;\n" +
                "int d = 3;", parts[2].data)
        Assert.assertEquals("multi line comment\n" +
                " of multi lines text", parts[3].data)
        Assert.assertEquals("    int e = 5;\n}", parts[4].data)
    }
}
