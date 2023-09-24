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

package org.testingisdocumenting.znai.preprocessor

import org.junit.Test

class RegexpBasedPreprocessorTest {
    @Test
    void "single line replace"() {
        def input = """Hello World

# 03 - State

some lines here"""

        def regexp = '#\\s+\\d+\\s*-\\s*(.*)'
        def replacement = '# $1'

        def result = new RegexpBasedPreprocessor("$regexp,$replacement").preprocess(input)
        result.should == """Hello World

# State

some lines here"""
    }

    @Test
    void "multi line replace"() {
        def input = """Hello World

```{=html}
<!-- \$MDX file=../../1 -->
```

```java
snippet
```

some lines here"""

        def regexp = '```\\{=html}.*?```'
        def replacement = ""

        def result = new RegexpBasedPreprocessor([new RegexpAndReplacement(regexp, replacement)]).preprocess(input)
        result.should == """Hello World



```java
snippet
```

some lines here"""
    }

    @Test
    void "todo"() {
        def input = """
This wouldn't be a programming tutorial without a hello world example,
which introduces the `Vdom.Node.text` node constructor.

```{=html}
<!-- \$MDX file=../../examples/bonsai_guide_code/vdom_examples.ml,part=hello_world -->
```

``` ocaml
let hello_world : Vdom.Node.t = Vdom.Node.text "hello world!"
```

```{=html}
<aside>
```
hello
contributions back to the main library!
```{=html}
</aside>
```

```{=html}
<iframe data-external="1" src="https://bonsai:8535#hello_world">
```
```{=html}
</iframe>
```
The text node will frequently be the "leaf" of a view (there are no
"children" of a text node). Let's put some text inside a bulleted list
by using some more node constructors:

```{=html}
<!-- \$MDX file=../../examples/bonsai_guide_code/vdom_examples.ml,part=hello_world -->
```

``` ocaml
let hello_world2 : Vdom.Node.t = Vdom.Node.text "hello world!"
```

```{=html}
<iframe data-external="1" src="https://bonsai:8535#hello_world">
```
```{=html}
</iframe>
```
"""
        def result = new RegexpBasedPreprocessor("```\\{=html}[^`]*?MDX[^']*?```,\n"  +
                "```\\{=html}\\s*<iframe.*?src=\"https://bonsai:8535([^\"]+)\"[^`]*?```, :include-iframe: ../../bonsai-build/\$1 {fit: true}\n" +
                "```\\{=html}\\s*</iframe[^`]*?```,\n" +
                "```\\{=html}\\s*<aside>\\s*```,```attention-note\n" +
                "```\\{=html}\\s*</aside>\\s*```,```\n" +
                "").preprocess(input)
//                "```\\{=html}.*?```,\n").preprocess(input)

        println result
    }

    @Test
    void "multi line extract and multiple regexp"() {
        def input = """Hello World

```{=html}
<iframe data-external="1" src="https://bonsai:8535#double-the-number-rpc">
```

```java
snippet
```

some lines here"""

        def regexp1 = '```\\{=html}.*?<iframe.*?src="([^"]+)".*?```'
        def replacement1 = ':include-iframe: $1'

        def result = new RegexpBasedPreprocessor("Hello World, Hey World\n" +
                "$regexp1,$replacement1").preprocess(input)
        result.should == """Hey World

:include-iframe: https://bonsai:8535#double-the-number-rpc

```java
snippet
```

some lines here"""
    }
}
