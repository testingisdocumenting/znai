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

package com.twosigma.znai.cpp.parser

import com.twosigma.utils.ResourceUtils
import org.junit.Assert
import org.junit.Test

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
}""", CppSourceCode.entryDefinition(code, "main"))
    }

    @Test
    void "extract method definition"() {
        Assert.assertEquals("""void ClassName::method_name() {
   b= 2;
}""", CppSourceCode.entryDefinition(code, "ClassName::method_name"))
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
int e = 5;""", CppSourceCode.entryBodyOnly(code, "main"))
    }

    @Test
    void "extract class definition"() {
        Assert.assertEquals("""class TestClass2 {
    //
    private:
    int s;

    void testMethod() {
        // inlined method
    }

    //comment
};""", CppSourceCode.entryDefinition(code, "TestClass2"))
    }

    @Test
    void "extract class definition body only"() {
        Assert.assertEquals("""//
private:
int s;

void testMethod() {
    // inlined method
}

//comment""", CppSourceCode.entryBodyOnly(code, "TestClass2"))
    }

    @Test
    void "extract enum definition"() {
        Assert.assertEquals("""enum TestEnum1 {
    COLOR1,
    // comments in between
    COLOR2
};""", CppSourceCode.entryDefinition(code, "TestEnum1"))
    }

    @Test
    void "split source code into groups using comments"() {
        def main = CppSourceCode.entryDefinition(code, "main")
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
