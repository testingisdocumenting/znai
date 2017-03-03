package com.twosigma.documentation.parser

import com.twosigma.documentation.codesnippets.CodeToken
import com.twosigma.documentation.codesnippets.CodeTokenizer

/**
 * @author mykola
 */
class TestCodeTokenizer implements CodeTokenizer {
    @Override
    List<CodeToken> tokenize(String lang, String code) {
        return Collections.singletonList(new CodeToken("text", code))
    }
}
