package com.twosigma.znai.codesnippets;

import java.util.List;

public interface CodeTokenizer {
    List<?> tokenize(String lang, String code);
}
