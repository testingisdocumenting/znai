package com.twosigma.documentation.codesnippets

import com.twosigma.documentation.TestNashornEngine
import org.junit.Test

/**
 * @author mykola
 */
class JsBasedCodeSnippetsTokenizerTest {
    @Test
    void "tokenizes snippet"() {
        def tokenizer = new JsBasedCodeSnippetsTokenizer(TestNashornEngine.instance)
        def tokens = tokenizer.tokenize("cpp", "int a = 2;")

        assert tokens == [[type:'keyword', content:'int'],
                          [type:'text', content:' a '],
                          [type:'operator', content: '='],
                          [type:'text', content: ' '],
                          [type:'number', content:'2'],
                          [type:'punctuation', content: ';']]
    }
}
