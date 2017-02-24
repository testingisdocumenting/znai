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

        assert tokens.collect { it.toMap() } == [[type:'keyword', data:'int'],
                                                 [type:'text', data:' a '],
                                                 [type:'operator', data: '='],
                                                 [type:'text', data: ' '],
                                                 [type:'number', data:'2'],
                                                 [type:'punctuation', data: ';']]
    }
}
