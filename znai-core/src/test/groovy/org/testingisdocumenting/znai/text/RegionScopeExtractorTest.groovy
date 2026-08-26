/*
 * Copyright 2023 znai maintainers
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

package org.testingisdocumenting.znai.text

import org.junit.Test

class RegionScopeExtractorTest {
    @Test
    void "one char scope that starts and new line"() {
        def result = extract(1, "[]", """let nums = 
[1,
2,
"[]",
4]
let something in,""")

        result.should == [1, 4]
    }

    @Test
    void "one char scope that starts and end of a line"() {
        def result = extract(0, "{}", """if (myCond) {
  {}
}
println "hello"
""")

        result.should == [0, 2]
    }

    @Test
    void "nested multi char scope"() {
        def result = extract(0, "let,in", """let my_var =
  let nested = 
     computation
  in   
in  
println "hello"
""")

        result.should == [0, 4]
    }

    @Test
    void "nested multi char scope should ignore incomplete words"() {
        def result = extract(1, "let,in", """
let my_var =
  letter nested = computing
  let nested_real = 
     5 computing
  in   
in  
println "hello"
""")

        result.should == [1, 6]
    }

    @Test
    void "ocaml type variables should not be treated as string start"() {
        def result = extract(0, "sig,end", """module type S = sig
  type 'a t
  val map : ('a -> 'b) -> 'a t -> 'b t
end
let x = 5
""")

        result.should == [0, 3]
    }

    @Test
    void "ocaml multiple type variables on one line should not mask scope"() {
        def result = extract(0, "{}", """type ('a, 'b) pair = {
  first: 'a;
  second: 'b;
}
type other = int
""")

        result.should == [0, 3]
    }

    @Test
    void "primed names should not be treated as quotes"() {
        def result = extract(0, "{}", """let f x' = {
  value = x' + 1;
}
let another = 2
""")

        result.should == [0, 2]
    }

    @Test
    void "rust lifetimes should not be treated as quotes"() {
        def result = extract(0, "{}", """fn foo<'a>(x: &'a str) -> &'a str {
    x
}
fn bar() {}
""")

        result.should == [0, 2]
    }

    @Test
    void "apostrophe in comments should not break scope"() {
        def result = extract(0, "{}", """function f() { // don't fail
  return 1;
}
""")

        result.should == [0, 2]
    }

    @Test
    void "char literals with scope chars inside should be ignored"() {
        def result = extract(0, "{}", """if (c == '{') {
  handle('\\'');
}
""")

        result.should == [0, 2]
    }

    @Test
    void "single quoted strings with scope chars inside should be ignored"() {
        def result = extract(0, "{}", """d = {
  'key{': 'value}'
}
""")

        result.should == [0, 2]
    }

    @Test
    void "scope char inside double quotes after apostrophe should be ignored"() {
        def result = extract(0, "{}", """if (cond) {
  print("don't } stop")
}
""")

        result.should == [0, 2]
    }

    private static List<Integer> extract(int startLineIdx, String scope, String text) {
        def extractor = new RegionScopeExtractor(TextLinesAccessor.createFromArray(text.split("\n")), startLineIdx, scope)
        extractor.process()

        return [extractor.resultStartLineIdx, extractor.resultEndLineIdx]
    }
}
