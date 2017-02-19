package com.twosigma.documentation.cpp

import com.twosigma.utils.ResourceUtils
import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class CppSourceCodeTest {
    @Test
    void "extract body with signature"() {
        String code = ResourceUtils.textContent("test.cpp");
        def body = CppSourceCode.methodBody(code, "main")
        Assert.assertEquals("""int main() {
    int test = 2;

    // comment
    // in two lines
    int b = 3;
}""", body)
    }
}
