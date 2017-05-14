package com.twosigma.documentation.parser

import com.twosigma.documentation.codesnippets.CodeTokenizer

/**
 * @author mykola
 */
class TestCodeTokenizer implements CodeTokenizer {
    @Override
    List<?> tokenize(String lang, String code) {
        return [[type: "text", content: code]]
    }
}
