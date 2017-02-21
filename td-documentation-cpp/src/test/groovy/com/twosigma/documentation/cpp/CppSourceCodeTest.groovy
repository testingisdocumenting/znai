package com.twosigma.documentation.cpp

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
}""", CppSourceCode.methodBody(code, "main"))
    }

    @Test
    void "extract method definition"() {
        Assert.assertEquals("""void ClassName::method_name() {
   b= 2;
}""", CppSourceCode.methodBody(code, "ClassName::method_name"))
    }

}
