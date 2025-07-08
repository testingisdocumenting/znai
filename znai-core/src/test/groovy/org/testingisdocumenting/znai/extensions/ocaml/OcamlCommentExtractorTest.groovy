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

package org.testingisdocumenting.znai.extensions.ocaml

import org.junit.Test
import org.testingisdocumenting.znai.parser.TestComponentsRegistry

import java.nio.file.Paths

class OcamlCommentExtractorTest {


    @Test
    void "extract single line comment block"() {
        def content = """
(* This is a comment *)
let x = 5
"""
        def extractor = new OcamlCommentExtractor(content)
        def result = extractor.extractCommentBlock("let x")
        result.should == "This is a comment"
    }

    @Test
    void "extract multi line comment block"() {
        def content = """
(* This is a 
   multi-line
   comment *)
let y = 10
"""
        def extractor = new OcamlCommentExtractor(content)
        def result = extractor.extractCommentBlock("let y")
        result.should == """This is a 
   multi-line
   comment"""
    }

    @Test
    void "extract comment block with code in between"() {
        def content = """
let a = 1
(* Comment for function *)
let myFunc x = x + 1
let b = 2
"""
        def extractor = new OcamlCommentExtractor(content)
        def result = extractor.extractCommentBlock("myFunc")
        result.should == "Comment for function"
    }

    @Test
    void "extract documentation comment with double asterisk"() {
        def content = """
(** This is a documentation comment *)
let docFunc x = x * 2
"""
        def extractor = new OcamlCommentExtractor(content)
        def result = extractor.extractCommentBlock("docFunc")
        result.should == "This is a documentation comment"
    }

    @Test(expected = IllegalArgumentException)
    void "throw exception when text not found"() {
        def content = """
(* Some comment *)
let x = 5
"""
        def extractor = new OcamlCommentExtractor(content)
        extractor.extractCommentBlock("nonexistent")
    }

    @Test(expected = IllegalArgumentException)
    void "throw exception when no comment block found before match"() {
        def content = """
let x = 5
let y = 10
"""
        def extractor = new OcamlCommentExtractor(content)
        extractor.extractCommentBlock("let x")
    }

    @Test
    void "convert OCaml inline code syntax to markdown"() {
        def extractor = new OcamlCommentExtractor("")
        
        extractor.processOcamlDocSyntax("Use [List.map] to transform").should == "Use `List.map` to transform"
        extractor.processOcamlDocSyntax("Check [fold_left] and [fold_right]").should == "Check `fold_left` and `fold_right`"
    }

    @Test
    void "convert OCaml multi-line code blocks to markdown"() {
        def extractor = new OcamlCommentExtractor("")
        
        def input = """Example:
{[
  let x = 1
  let y = 2
]}"""
        
        def expected = """Example:

```
let x = 1
  let y = 2
```
"""
        
        extractor.processOcamlDocSyntax(input).should == expected
    }

    @Test
    void "handle multiple code blocks"() {
        def extractor = new OcamlCommentExtractor("")
        
        def input = """First: {[let x = 1]}
Second: {[let y = 2]}"""
        
        def expected = """First: 
```
let x = 1
```

Second: 
```
let y = 2
```
"""
        
        extractor.processOcamlDocSyntax(input).should == expected
    }

    @Test
    void "handle mixed OCaml and markdown syntax"() {
        def extractor = new OcamlCommentExtractor("")
        
        def input = """## Header

Use [fold_left] like this:
{[
  List.fold_left (+) 0 [1; 2; 3]
]}

after"""
        
        def expected = """## Header

Use `fold_left` like this:

```
List.fold_left (+) 0 [1; 2; 3]
```

after"""
        
        extractor.processOcamlDocSyntax(input).should == expected
    }

    @Test
    void "convert full comment block to doc elements"() {
        def content = """
(** Use [List.map] to transform elements *)
let transform lst = List.map (fun x -> x + 1) lst
"""
        def extractor = new OcamlCommentExtractor(content)
        def elements = extractor.extractCommentBlockAsDocElements(TestComponentsRegistry.TEST_COMPONENTS_REGISTRY,
                Paths.get("test.ml"), "transform")
        
        elements.size().should == 1
        elements[0].toMap().type.should == 'TestMarkup'
        elements[0].toMap().markup.should == 'Use `List.map` to transform elements'
    }
}