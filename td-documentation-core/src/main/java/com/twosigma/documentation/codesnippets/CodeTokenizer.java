package com.twosigma.documentation.codesnippets;

import java.util.List;

/**
 * @author mykola
 */
public interface CodeTokenizer {
    List<?> tokenize(String lang, String code);
}
