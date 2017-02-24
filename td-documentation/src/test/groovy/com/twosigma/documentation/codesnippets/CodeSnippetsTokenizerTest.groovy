package com.twosigma.documentation.codesnippets

import com.twosigma.documentation.TestNashornEngine
import org.junit.Test

/**
 * @author mykola
 */
class CodeSnippetsTokenizerTest {
    @Test
    void "tokenizes snippet"() {
        def tokenizator = new CodeSnippetsTokenizer(TestNashornEngine.instance)
        def tokens = tokenizator.tokenize("int a = 2;")

        assert tokens == [[type:'keyword', data:'int'],
                          [type:'text', data:' a '],
                          [type:'operator', data: '='],
                          [type:'text', data: ' '],
                          [type:'number', data:'2'],
                          [type:'punctuation', data: ';']]
    }
}
