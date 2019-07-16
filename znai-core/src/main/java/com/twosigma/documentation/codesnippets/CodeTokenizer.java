package com.twosigma.documentation.codesnippets;

import java.util.List;

public interface CodeTokenizer {
    List<?> tokenize(String lang, String code);
}
