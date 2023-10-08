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

    private static List<Integer> extract(int startLineIdx, String scope, String text) {
        def extractor = new RegionScopeExtractor(TextLinesAccessor.createFromArray(text.split("\n")), startLineIdx, scope)
        extractor.process()

        return [extractor.resultStartLineIdx, extractor.resultEndLineIdx]
    }
}
